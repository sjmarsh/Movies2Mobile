package com.sjmarsh.movies2mobile.ui.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ref: https://medium.com/android-news/implementing-search-on-type-in-android-with-coroutines-ab117c8f13a4
class DebouncingQueryTextListener(lifecycle: Lifecycle, private val onDebouncingQueryTextChange: (String?) -> Unit) : SearchView.OnQueryTextListener {
    private val debouncePeriod: Long = 800
    private val coroutineScope = lifecycle.coroutineScope
    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(newText)
            }
        }
        return false
    }
}