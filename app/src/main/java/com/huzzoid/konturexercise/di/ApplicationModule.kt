package com.huzzoid.konturexercise.di

import android.app.Application
import com.huzzoid.konturexercise.ui.app.KonturExerciseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun providesRouter(application: Application) =
        (application as KonturExerciseApplication).cicerone.router

    @Provides
    fun providesNavigationHolder(application: Application) =
        (application as KonturExerciseApplication).cicerone.getNavigatorHolder()
}