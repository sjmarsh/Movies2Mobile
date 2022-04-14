package com.example.movies2mobile.ui.concerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel

class ConcertViewModel(dataService: DataService) : ViewModel() {

    private final val _dataService: DataService = dataService

    var searchText : String = ""
    var concertList = listOf<MovieModel>()

    fun searchConcerts() {
        concertList = _dataService.searchConcerts(searchText)
    }
}