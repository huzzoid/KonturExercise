package com.huzzoid.konturexercise.ui.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.terrakok.cicerone.Router
import com.huzzoid.konturexercise.domain.details.DetailsEvent
import com.huzzoid.konturexercise.domain.details.DetailsInteractor
import com.huzzoid.konturexercise.domain.details.DetailsState
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.huzzoid.konturexercise.ui.screens.Screen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

internal class DetailsViewModel @ViewModelInject constructor(
    repository: ContactsRepository,
    private val router: Router,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val interactor =
        DetailsInteractor(repository, savedStateHandle.get<DetailsParcelable>(ARGS)!!.toState())

    private val _state = MutableLiveData<DetailsState>()
    internal val state: LiveData<DetailsState> = _state
    private val _events = MutableLiveData<DetailsEvent>()
    internal val events: LiveData<DetailsEvent> = _events

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

    fun back() {
        router.exit()
    }

    fun onPhoneClick(phone: String) {
        router.navigateTo(Screen.callToPhone(phone))
    }

    private fun unexpectedError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}