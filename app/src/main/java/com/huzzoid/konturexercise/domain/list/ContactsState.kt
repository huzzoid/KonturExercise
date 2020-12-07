package com.huzzoid.konturexercise.domain.list

import com.huzzoid.konturexercise.domain.pojo.Contact

data class ContactsState(
    val loading: Loading = Loading.FULL,
    val originalList: List<Contact> = emptyList(),
    val filteredOutList: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val error: Throwable? = null
)

enum class Loading {
    FULL, S2R, NONE
}