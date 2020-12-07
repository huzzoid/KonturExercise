package com.huzzoid.konturexercise.di

import com.huzzoid.konturexercise.data.sources.ContactsSourceImpl
import com.huzzoid.konturexercise.domain.sources.ContactsSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {
    @Binds
    fun providesContactsSource(repo: ContactsSourceImpl): ContactsSource
}