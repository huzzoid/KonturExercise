package com.huzzoid.konturexercise.domain.contacts

sealed class ContactsEvent {
    data class PartialError(val error: Throwable) : ContactsEvent()
    data class FullError(val error: Throwable) : ContactsEvent()
}
