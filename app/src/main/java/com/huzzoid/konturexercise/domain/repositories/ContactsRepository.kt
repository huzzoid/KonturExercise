package com.huzzoid.konturexercise.domain.repositories

import com.huzzoid.konturexercise.domain.pojo.Contact

interface ContactsRepository {
    suspend fun loadContacts(): List<Contact>
}