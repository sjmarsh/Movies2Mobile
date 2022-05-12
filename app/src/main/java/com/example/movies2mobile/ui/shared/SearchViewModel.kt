package com.example.movies2mobile.ui.shared

import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel

class SearchViewModel(dataService: DataService) {
    private final val _dataService: DataService = dataService

    var searchText : String = ""
    var searchContext: SearchContext? = SearchContext.MOVIE
    var searchResults = listOf<MovieModel>()

    fun search() {
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.searchMovies(searchText)
            SearchContext.CONCERT -> searchResults = _dataService.searchConcerts(searchText)
            else -> { println("Search Context not supported")} // TODO error logging
        }
    }
}