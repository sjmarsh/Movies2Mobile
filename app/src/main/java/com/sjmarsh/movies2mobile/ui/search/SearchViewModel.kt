package com.sjmarsh.movies2mobile.ui.search

import android.util.Log
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.models.ModelBase

class SearchViewModel(dataService: IDataService) {
    private val _dataService: IDataService = dataService

    var searchText : String? = ""
    var categoryFilter: String? = null // TODO make this generic
    var movieSortBy: MovieSortBy? = null // TODO make this generic
    var searchContext: SearchContext? = SearchContext.MOVIE
    var searchResults = listOf<ModelBase>()  // TODO make this more generic
    var hasSearchResults : Boolean = searchResults.isNotEmpty()

    suspend fun search() {
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.searchMovies(searchText, categoryFilter, movieSortBy)
            SearchContext.ACTOR -> searchResults = _dataService.searchActors(searchText)
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }

    suspend fun searchById(id: Int?){
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.getMoviesByMovieId(id)
            SearchContext.ACTOR -> searchResults = _dataService.getActorsById(id)
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }
}