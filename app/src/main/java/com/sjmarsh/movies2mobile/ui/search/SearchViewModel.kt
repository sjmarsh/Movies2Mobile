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

    private val PAGE_SIZE: Int = 30
    private var skipMovies: Int = 0
    private var skipActors: Int = 0
    private var totalResultCount: Int = 0
    private var movieResults = mutableListOf<ModelBase>()
    private var actorResults = mutableListOf<ModelBase>()

    suspend fun search() {
        when(searchContext) {
            SearchContext.MOVIE -> {
                skipMovies = 0
                movieResults.clear()
                val result = _dataService.searchMovies(searchText, categoryFilter, movieSortBy, skipMovies, PAGE_SIZE)
                totalResultCount = result.totalCount
                searchResults = result.result
                skipMovies += PAGE_SIZE
            }
            SearchContext.ACTOR -> {
                skipActors = 0
                actorResults.clear()
                val result = _dataService.searchActors(searchText, skipActors, PAGE_SIZE)
                totalResultCount = result.totalCount
                searchResults = result.result
                skipActors += PAGE_SIZE
            }
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }

    suspend fun scrollSearch() {
        when(searchContext) {
            SearchContext.MOVIE -> {
                val result = _dataService.searchMovies(searchText, categoryFilter, movieSortBy, skipMovies, PAGE_SIZE)
                movieResults.addAll(result.result)
                totalResultCount = result.totalCount
                searchResults = movieResults
                skipMovies += PAGE_SIZE
            }
            SearchContext.ACTOR -> {
                val result = _dataService.searchActors(searchText, skipActors, PAGE_SIZE)
                actorResults.addAll(result.result)
                totalResultCount = result.totalCount
                searchResults = actorResults
                skipActors += PAGE_SIZE
            }
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

    fun isAllDataLoaded(): Boolean {
        var isAllLoaded = false
        when(searchContext) {
            SearchContext.MOVIE -> isAllLoaded = skipMovies + PAGE_SIZE > totalResultCount
            SearchContext.ACTOR -> isAllLoaded = skipActors + PAGE_SIZE > totalResultCount
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
        return isAllLoaded
    }
}