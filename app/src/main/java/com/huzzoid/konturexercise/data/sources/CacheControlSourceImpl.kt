package com.huzzoid.konturexercise.data.sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.huzzoid.konturexercise.domain.sources.CacheControlSource
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val LAST_UPDATE_TIME_KEY = "last_update_time"

class CacheControlSourceImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    CacheControlSource {

    private val cacheTtl = TimeUnit.MINUTES.toMillis(1L)

    override fun isValid() = Single.fromCallable {
        System.currentTimeMillis() - sharedPreferences.getLong(LAST_UPDATE_TIME_KEY, 0L) <= cacheTtl
    }

    override fun setCacheLatestUpdateTimeBlocking(currentTimeMillis: Long) {
        sharedPreferences.edit {
            putLong(LAST_UPDATE_TIME_KEY, currentTimeMillis)
            commit()
        }
    }
}