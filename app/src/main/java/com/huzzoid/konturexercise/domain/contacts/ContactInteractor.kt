package com.huzzoid.konturexercise.domain.contacts

import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent.FullError
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent.PartialError
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.huzzoid.konturexercise.domain.repositories.isFullError
import com.huzzoid.konturexercise.domain.repositories.isPartialError
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.rxkotlin.zipWith
import java.util.concurrent.TimeUnit

private const val PAGE_SIZE = 100
private const val LOADING_TRIGGER_ELEMENT_FROM_END = 10

internal class ContactInteractor(
    initialState: ContactsState,
    private val repository: ContactsRepository
) {

    private val disposables = CompositeDisposable()

    private val stateRelay = BehaviorRelay.createDefault(initialState).toSerialized()
    private val eventsRelay: PublishRelay<ContactsEvent> = PublishRelay.create()
    private val lastVisibleItemNumberRegistry: PublishRelay<Int> = PublishRelay.create()
    private var searchJob: Disposable? = null

    init {
        stateRelay.accept(initialState.copy(loading = Loading.FULL))
        init(false)
        disposables += createPageListener()
    }

    fun stateObservable(): Observable<ContactsState> = stateRelay.hide().distinctUntilChanged()

    fun eventsObservable(): Observable<ContactsEvent> = eventsRelay.hide()

    fun refresh() {
        disposables += stateRelay.firstOrError()
            .doOnSuccess {
                stateRelay.accept(it.copy(loading = Loading.S2R))
            }.subscribe({ init(true) }, this::unexpectedError)
    }

    fun doSearch(searchQuery: String?) {
        searchJob?.dispose()
        searchJob = stateRelay.firstOrError()
            .doOnSuccess {
                stateRelay.accept(it.copy(searchQuery = searchQuery))
            }
            .flatMap { repository.getFilteredContacts(searchQuery, PAGE_SIZE, 0) }
            .zipWith(stateRelay.firstOrError())
            .subscribe({ (result, state) ->
                stateRelay.accept(
                    state.copy(
                        filteredOutList = result,
                        isPagingAvailable = result.size == PAGE_SIZE
                    )
                )
            }, this::unexpectedError)
    }

    fun dispose() {
        searchJob?.dispose()
        disposables.dispose()
    }

    private fun createPageListener() =
        lastVisibleItemNumberRegistry.debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .withLatestFrom(stateRelay)
            .filter { (id, state) -> state.isPagingAvailable && (state.filteredOutList.size - id) < LOADING_TRIGGER_ELEMENT_FROM_END }
            .switchMap { (_, state) ->
                repository.getFilteredContacts(
                    state.searchQuery,
                    PAGE_SIZE,
                    (state.filteredOutList.size / PAGE_SIZE) * PAGE_SIZE
                ).toObservable()
            }
            .withLatestFrom(stateRelay)
            .subscribe({ (list, state) ->
                stateRelay.accept(
                    state.copy(
                        filteredOutList = state.filteredOutList + list,
                        isPagingAvailable = list.size == PAGE_SIZE
                    )
                )
            }, this::unexpectedError)

    private fun init(force: Boolean) {
        disposables += repository.updateContacts(force)
            .zipWith(stateRelay.firstOrError())
            .doOnSuccess { (response, state) ->
                when {
                    response.isPartialError() -> {
                        eventsRelay.accept(PartialError(response.errors.first()))
                    }
                    response.isFullError() -> {
                        eventsRelay.accept(FullError(response.errors.first()))
                    }
                }
                stateRelay.accept(state.copy(loading = Loading.NONE))
            }
            .subscribe(
                { (_, state) -> doSearch(state.searchQuery) },
                this::unexpectedError
            )
    }

    fun loadNextPageIfNecessary(visibleItemPosition: Int) {
        lastVisibleItemNumberRegistry.accept(visibleItemPosition)
    }

    private fun unexpectedError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}