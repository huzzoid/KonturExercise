package com.huzzoid.konturexercise.ui.app

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KonturExerciseApplication : Application() {

    internal val cicerone = Cicerone.create()
}