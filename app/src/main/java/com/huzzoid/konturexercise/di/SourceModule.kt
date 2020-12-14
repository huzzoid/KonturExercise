package com.huzzoid.konturexercise.di

import com.huzzoid.konturexercise.data.sources.CacheControlSourceImpl
import com.huzzoid.konturexercise.data.sources.ContactsNetSourceImpl
import com.huzzoid.konturexercise.domain.sources.CacheControlSource
import com.huzzoid.konturexercise.domain.sources.ContactsNetSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {
    @Binds
    fun providesContactsSource(src: ContactsNetSourceImpl): ContactsNetSource

    @Binds
    fun providesCacheControlSource(src: CacheControlSourceImpl): CacheControlSource
}