package com.huzzoid.konturexercise.ui.details

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

class DetailsViewModel @ViewModelInject constructor() : ViewModel(), LifecycleObserver {

    init {
        Log.d("DetailsViewModel", "DetailsViewModel $this")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("DetailsViewModel", "DetailsViewModel dispose $this")
    }
}