package com.huzzoid.konturexercise.domain.details

import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.zipWith

class DetailsInteractor(
    repository: ContactsRepository,
    initialState: DetailsState
) {

    private val disposables = CompositeDisposable()
    private val stateRelay = BehaviorRelay.createDefault(initialState).toSerialized()
    private val eventsRelay: PublishRelay<DetailsEvent> = PublishRelay.create()

    init {
        disposables += repository.getContactById(initialState.initialId)
            .zipWith(stateRelay.firstOrError())
            .subscribe({ (contact, state) ->
                stateRelay.accept(
                    state.copy(
                        loading = Loading.NONE,
                        contact = contact
                    )
                )
            }, {
                eventsRelay.accept(DetailsEvent.Error(it))
            })
    }

    fun stateObservable(): Observable<DetailsState> = stateRelay.hide().distinctUntilChanged()

    fun dispose() {
        disposables.dispose()
    }

    fun eventsObservable() = eventsRelay.hide()
}