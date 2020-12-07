package com.huzzoid.konturexercise.ui.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.huzzoid.konturexercise.ui.screens.Screen

class MainActivityViewModel @ViewModelInject constructor(private val router: Router) : ViewModel(),
    LifecycleObserver {

    fun onCreate() {
        router.newRootScreen(Screen.contactsScreen())
    }
}