package com.huzzoid.konturexercise.data.net

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Net {
    @GET("generated-{id}.json")
    fun loadPart(@Path("id") id: String): Single<List<ContactDto>>
}