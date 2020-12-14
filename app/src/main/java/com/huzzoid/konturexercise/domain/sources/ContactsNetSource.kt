package com.huzzoid.konturexercise.domain.sources

import com.huzzoid.konturexercise.data.net.ContactDto
import io.reactivex.Single

interface ContactsNetSource {
    fun getContactsPart(id: String): Single<List<ContactDto>>
}
