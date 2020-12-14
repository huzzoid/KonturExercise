package com.huzzoid.konturexercise.domain.sources

import io.reactivex.Single

interface CacheControlSource {
    fun isValid(): Single<Boolean>
    fun setCacheLatestUpdateTimeBlocking(currentTimeMillis: Long)
}
