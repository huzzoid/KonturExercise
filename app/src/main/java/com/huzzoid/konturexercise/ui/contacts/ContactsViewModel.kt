package com.huzzoid.konturexercise.ui.contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.terrakok.cicerone.Router
import com.huzzoid.konturexercise.domain.list.ContactsState
import com.huzzoid.konturexercise.domain.list.Loading
import com.huzzoid.konturexercise.domain.pojo.Contact
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.huzzoid.konturexercise.ui.screens.Screen
import kotlinx.coroutines.*

private const val ERROR_TIMEOUT = 1000L

class ContactsViewModel @ViewModelInject constructor(
    private val router: Router,
    private val contactsRepository: ContactsRepository
) : ViewModel(), LifecycleObserver {

    private val _state = MutableLiveData<ContactsState>()
    internal val state: LiveData<ContactsState> = _state.also {
        it.value = ContactsState()
    }

    private var searchJob: Job? = null

    init {
        _state.value = state.value?.copy(loading = Loading.FULL)
        loadContacts()
    }

    fun goToDetails(contact: Contact) {
        router.navigateTo(Screen.detailsScreen())
    }

    fun onSwipeToRefresh() {
        _state.value = state.value?.copy(loading = Loading.S2R)
        loadContacts()
    }

    fun onSearchQueryChanged(searchQuery: String) {
        _state.value = state.value?.copy(searchQuery = searchQuery)
        searchJob?.cancel()
        val list = _state.value?.originalList?.toList()
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            val filteredList = if (searchQuery.isBlank()) {
                list
            } else {
                list?.filter {
                    it.name.contains(searchQuery, true) ||
                            it.phone.contains(searchQuery, true) ||
                            it.height.contains(searchQuery)
                }
            }
            withContext(Dispatchers.Main) {
                _state.value = _state.value?.copy(filteredOutList = filteredList ?: emptyList())
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            try {
                val list = contactsRepository.loadContacts()
                _state.value = state.value?.copy(
                    loading = Loading.NONE,
                    originalList = list,
                    searchQuery = "",
                    filteredOutList = list
                )
            } catch (e: Exception) {
                _state.value = state.value?.copy(loading = Loading.NONE, error = e)
                launch {
                    delay(ERROR_TIMEOUT)
                    _state.value = state.value?.copy(error = null)
                }
            }
        }
    }
}