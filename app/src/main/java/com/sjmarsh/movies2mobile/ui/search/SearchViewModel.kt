package com.sjmarsh.movies2mobile.ui.search

import android.util.Log
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.models.ModelBase
import com.sjmarsh.movies2mobile.models.SearchResult

class SearchViewModel(dataService: IDataService) {
    private val _dataService: IDataService = dataService

    var searchText : String? = ""
    var categoryFilter: String? = null // TODO make this generic
    var movieSortBy: MovieSortBy? = null // TODO make this generic
    var searchContext: SearchContext? = SearchContext.MOVIE
    var searchResults = mutableListOf<ModelBase>()
    var hasSearchResults : Boolean = searchResults.isNotEmpty()

    private var skipItems: Int = 0
    private var totalResultCount: Int = 0
    private val PAGE_SIZE: Int = 30

    suspend fun search() {
        skipItems = 0
        when(searchContext) {
            SearchContext.MOVIE -> {
                val result = _dataService.searchMovies(searchText, categoryFilter, movieSortBy, skipItems, PAGE_SIZE)
                totalResultCount = result.totalCount
                searchResults = result.result.toMutableList()
            }
            SearchContext.ACTOR -> {
                val result = _dataService.searchActors(searchText, skipItems, PAGE_SIZE)
                totalResultCount = result.totalCount
                searchResults = result.result.toMutableList()
            }
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }

        skipItems += PAGE_SIZE
    }

    suspend fun scrollSearch() {
        when(searchContext) {
            SearchContext.MOVIE -> {
                val result = _dataService.searchMovies(searchText, categoryFilter, movieSortBy, skipItems, PAGE_SIZE)
                searchResults.addAll(result.result)
                totalResultCount = result.totalCount

            }
            SearchContext.ACTOR -> {
                val result = _dataService.searchActors(searchText, skipItems, PAGE_SIZE)
                searchResults.addAll(result.result)
                totalResultCount = result.totalCount
            }
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
        skipItems += PAGE_SIZE
    }

    suspend fun searchById(id: Int?){
        when(searchContext) {
            SearchContext.MOVIE -> searchResults = _dataService.getMoviesByMovieId(id).toMutableList()
            SearchContext.ACTOR -> searchResults = _dataService.getActorsById(id).toMutableList()
            else -> { Log.e("SearchViewModel","Search Context not supported")}
        }
    }

    fun isAllDataLoaded(): Boolean {
        return skipItems + PAGE_SIZE > totalResultCount
    }
}