package com.huzzoid.konturexercise.data.net

import retrofit2.http.GET

interface Net {
    @GET("generated-01.json")
    suspend fun load01(): List<ContactDto>

    @GET("generated-02.json")
    suspend fun load02(): List<ContactDto>

    @GET("generated-03.json")
    suspend fun load03(): List<ContactDto>
}