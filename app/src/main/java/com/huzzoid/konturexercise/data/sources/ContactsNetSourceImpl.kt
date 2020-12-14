package com.huzzoid.konturexercise.data.sources

import com.huzzoid.konturexercise.data.net.Net
import com.huzzoid.konturexercise.domain.sources.ContactsNetSource
import javax.inject.Inject

// todo private const val TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX"

class ContactsNetSourceImpl @Inject constructor(private val net: Net) : ContactsNetSource {

    override fun getContactsPart(id: String) = net.loadPart(id)
}