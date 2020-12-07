package com.huzzoid.konturexercise.ui.widget

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

private const val debouncePeriod = 300L

internal class DebouncingQueryTextListener(
    lifecycle: Lifecycle,
    private val onDebouncingQueryTextChange: (String) -> Unit
) : SearchView.OnQueryTextListener, LifecycleObserver {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var searchJob: Job? = null

    init {
        lifecycle.addObserver(this)
    }

    override fun onQueryTextSubmit(query: String) = true

    override fun onQueryTextChange(newText: String): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(debouncePeriod)
            onDebouncingQueryTextChange(newText)
        }
        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        searchJob?.cancel()
    }
}