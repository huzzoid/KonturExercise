package com.huzzoid.konturexercise.data.repositories

import com.huzzoid.konturexercise.data.db.ContactsDao
import com.huzzoid.konturexercise.data.db.toDomain
import com.huzzoid.konturexercise.data.net.toEntity
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import com.huzzoid.konturexercise.domain.repositories.UpdateContactsResponse
import com.huzzoid.konturexercise.domain.sources.CacheControlSource
import com.huzzoid.konturexercise.domain.sources.ContactsNetSource
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val cacheControlSource: CacheControlSource,
    private val contactsDao: ContactsDao,
    private val contactsNetSource: ContactsNetSource
) : ContactsRepository {

    override fun getFilteredContacts(
        searchQuery: String?,
        limit: Int,
        offset: Int
    ) = Single.fromCallable {
        if (searchQuery.isNullOrBlank()) {
            contactsDao.queryAll(limit, offset)
        } else {
            contactsDao.queryByNameOrPhone(searchQuery, limit, offset)
        }
    }.map { it.map { it.toDomain() } }
        .subscribeOn(Schedulers.io())

    override fun updateContacts(forceUpdate: Boolean): Single<UpdateContactsResponse> {
        val cacheIsValidSingle = if (forceUpdate) {
            Single.just(false)
        } else {
            cacheControlSource.isValid()
        }
        return cacheIsValidSingle.flatMap {
            if (it) {
                Single.just(UpdateContactsResponse())
            } else {
                Single.fromCallable {
                    contactsDao.deleteAll()
                }.flatMap {
                    Singles.zip(
                        createContactsPartLoadingSingle("01"),
                        createContactsPartLoadingSingle("02"),
                        createContactsPartLoadingSingle("03")
                    ).map { (result01, result02, result03) ->
                        val mutableList = mutableListOf<Throwable>()
                        result01.throwable?.let {
                            mutableList.add(it)
                        }
                        result02.throwable?.let {
                            mutableList.add(it)
                        }
                        result03.throwable?.let {
                            mutableList.add(it)
                        }
                        UpdateContactsResponse(mutableList)
                    }.flatMap {
                        if (it.errors.isEmpty()) {
                            cacheControlSource.setCacheLatestUpdateTimeBlocking(System.currentTimeMillis())
                        }
                        Single.just(it)
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun getContactById(id: String) = Single.fromCallable {
        contactsDao.queryById(id).first().toDomain()
    }.subscribeOn(Schedulers.io())

    private fun createContactsPartLoadingSingle(id: String) = contactsNetSource.getContactsPart(id)
        .map {
            contactsDao.insert(it.map { it.toEntity() })
            PartLoadResult()
        }
        .onErrorResumeNext { Single.just(PartLoadResult(it)) }

}

private data class PartLoadResult(val throwable: Throwable? = null)