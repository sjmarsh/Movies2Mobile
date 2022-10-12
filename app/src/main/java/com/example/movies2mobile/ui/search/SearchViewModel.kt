package com.example.movies2mobile.ui.search

import android.util.Log
import com.example.movies2mobile.data.IDataService
import com.example.movies2mobile.models.ModelBase
import com.example.movies2mobile.models.MovieModel

class SearchViewModel(dataService: IDataService) {
    private val _dataService: IDataService = dataService

    var searchText : String? = ""
    var categoryFilter: String? = null // TODO make this generic
    var searchContext: SearchContext? = SearchContext.MOVIE
    var searchResults = listOf<ModelBase>()  // TODO make this more generic
    var hasSearchResults : Boolean = searchResults.isNotEmpty()

    fun search() {
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.searchMovies(searchText, categoryFilter)
            SearchContext.ACTOR -> searchResults = _dataService.searchActors(searchText)
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }

    fun searchById(id: Int?){
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.getMoviesByMovieId(id)!!
            SearchContext.ACTOR -> searchResults = _dataService.getActorsById(id)!!
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }
}