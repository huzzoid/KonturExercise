package com.huzzoid.konturexercise.di

import com.huzzoid.konturexercise.data.repositories.ContactsRepositoryImpl
import com.huzzoid.konturexercise.domain.repositories.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun providesContactsRepository(repo: ContactsRepositoryImpl): ContactsRepository
}