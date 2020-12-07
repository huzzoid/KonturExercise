package com.huzzoid.konturexercise.data.sources

import com.huzzoid.konturexercise.data.net.ContactDto
import com.huzzoid.konturexercise.data.net.Net
import com.huzzoid.konturexercise.domain.pojo.Contact
import com.huzzoid.konturexercise.domain.sources.ContactsSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX"

class ContactsSourceImpl @Inject constructor(private val net: Net) : ContactsSource {

    override suspend fun getContacts(): List<Contact> {
        var list = emptyList<Contact>()
        coroutineScope {
            val elements1 = async { net.load01().map { mapContact(it, getSdf()) } }
            val elements2 = async { net.load02().map { mapContact(it, getSdf()) } }
            val elements3 = async { net.load03().map { mapContact(it, getSdf()) } }
            list = mutableListOf<Contact>().apply {
                addAll(elements1.await())
                addAll(elements2.await())
                addAll(elements3.await())
            }
        }
        return list
    }

    private fun mapContact(dto: ContactDto, sdf: SimpleDateFormat) =
        Contact(
            id = dto.id,
            name = dto.name,
            height = dto.height,
            phone = dto.phone,
            biography = dto.biography,
            temperament = dto.temperament,
            start = sdf.parse(dto.educationPeriod.start)!!,
            end = sdf.parse(dto.educationPeriod.end)!!
        )

    private fun getSdf(): SimpleDateFormat =
        SimpleDateFormat(TIME_PATTERN, Locale.ROOT)
}
