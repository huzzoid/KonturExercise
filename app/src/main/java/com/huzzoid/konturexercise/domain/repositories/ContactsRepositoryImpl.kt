package com.huzzoid.konturexercise.domain.repositories

import com.huzzoid.konturexercise.domain.sources.ContactsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val contactsSource: ContactsSource) : ContactsRepository {

    override suspend fun loadContacts() = withContext(Dispatchers.IO) {
        contactsSource.getContacts()
    }
}