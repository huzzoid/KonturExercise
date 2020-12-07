package com.huzzoid.konturexercise.domain.sources

import com.huzzoid.konturexercise.domain.pojo.Contact

interface ContactsSource {
    suspend fun getContacts(): List<Contact>
}
