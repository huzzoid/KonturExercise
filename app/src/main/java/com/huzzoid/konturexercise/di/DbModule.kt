package com.huzzoid.konturexercise.di

import android.content.Context
import androidx.room.Room
import com.huzzoid.konturexercise.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "KonturExercise"
        ).build()

    @Provides
    fun provideContactsDao(appDatabase: AppDatabase) = appDatabase.contactsDao()
}