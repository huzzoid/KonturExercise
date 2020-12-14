package com.huzzoid.konturexercise.ui.contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.huzzoid.konturexercise.domain.contacts.ContactInteractor
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent
import com.huzzoid.konturexercise.domain.contacts.ContactsState
import com.huzzoid.konturexercise.domain.pojo.Contact
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.huzzoid.konturexercise.ui.screens.Screen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

internal class ContactsViewModel @ViewModelInject constructor(
    repository: ContactsRepository,
    private val router: Router
) : ViewModel(),
    LifecycleObserver {

    private val interactor = ContactInteractor(ContactsState(), repository)

    private val _state = MutableLiveData<ContactsState>()
    internal val state: LiveData<ContactsState> = _state
    private val _events = MutableLiveData<ContactsEvent>()
    internal val events: LiveData<ContactsEvent> = _events

    private val disposables = CompositeDisposable()

    init {
        disposables += interactor.stateObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _state.postValue(it) }, ::unexpectedError)
        disposables += interactor.eventsObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _events.postValue(it) }, ::unexpectedError)
    }

    override fun onCleared() {
        interactor.dispose()
        disposables.dispose()
        super.onCleared()
    }

    fun onSwipeToRefresh() {
        interactor.refresh()
    }

    fun goToDetails(contact: Contact) {
        router.navigateTo(Screen.detailsScreen(contact.id))
    }

    fun onSearchQueryChanged(query: String) {
        interactor.doSearch(query)
    }

    fun onItemIsBeingVisible(visibleItemPosition: Int) {
        interactor.loadNextPageIfNecessary(visibleItemPosition)
    }

    private fun unexpectedError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
