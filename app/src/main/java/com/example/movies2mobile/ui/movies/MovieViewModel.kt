package com.example.movies2mobile.ui.movies
import androidx.lifecycle.ViewModel
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel

class MovieViewModel(dataService: DataService) : ViewModel() {

    private final val _dataService: DataService = dataService

    var searchText : String = ""
    var movieList = listOf<MovieModel>()

    fun searchMovies() {
        movieList = _dataService.searchMovies(searchText)
    }
}