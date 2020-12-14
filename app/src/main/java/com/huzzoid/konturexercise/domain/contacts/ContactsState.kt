package com.huzzoid.konturexercise.domain.contacts

import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.pojo.Contact

data class ContactsState(
    val loading: Loading = Loading.FULL,
    val filteredOutList: List<Contact> = emptyList(),
    val searchQuery: String? = "",
    val isPagingAvailable: Boolean = false
)
