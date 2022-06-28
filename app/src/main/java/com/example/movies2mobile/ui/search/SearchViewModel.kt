package com.example.movies2mobile.ui.search

import android.util.Log
import com.example.movies2mobile.data.IDataService
import com.example.movies2mobile.models.MovieModel

class SearchViewModel(dataService: IDataService) {
    private val _dataService: IDataService = dataService

    var searchText : String? = ""
    var categoryFilter: String? = null // TODO make this generic
    var searchContext: SearchContext? = SearchContext.MOVIE
    var searchResults = listOf<MovieModel>()  // TODO make this more generic
    var hasSearchResults : Boolean = searchResults.isNotEmpty()

    fun search() {
        when(searchContext) {
            // TODO make this more generic
            SearchContext.MOVIE -> searchResults = _dataService.searchMovies(searchText, categoryFilter)
            SearchContext.CONCERT -> searchResults = _dataService.searchConcerts(searchText)
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }
}