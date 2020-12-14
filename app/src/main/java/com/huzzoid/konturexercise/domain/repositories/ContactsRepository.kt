package com.huzzoid.konturexercise.domain.repositories

import com.huzzoid.konturexercise.domain.pojo.Contact
import io.reactivex.Single

interface ContactsRepository {

    fun getFilteredContacts(searchQuery: String?, limit: Int, offset: Int): Single<List<Contact>>

    fun getContactById(id: String): Single<Contact>

    fun updateContacts(forceUpdate: Boolean): Single<UpdateContactsResponse>
}