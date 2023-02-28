package com.sjmarsh.movies2mobile.ui.search

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.models.ModelBase
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SearchResultComponent(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), KoinComponent
{
    private val _dataService by inject<IDataService>()
    private val _searchContext: SearchContext?

    init {
        inflate(context, R.layout.search_result_component, this)

        val lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if(lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        _searchContext = getSearchContext(context, attrs)

        search("")
    }

    fun search(query: String?, categoryFilter: String? = null, movieSortBy: MovieSortBy? = null) : Boolean {

        val searchViewModel = SearchViewModel(_dataService)
        searchViewModel.searchText = query
        searchViewModel.categoryFilter = categoryFilter
        searchViewModel.movieSortBy = movieSortBy
        searchViewModel.searchContext = _searchContext

        runBlocking {
            searchViewModel.search()
        }

        updateSearchResults(searchViewModel)
        return searchViewModel.hasSearchResults
    }

    fun searchById(id: Int?) : Boolean {
        val searchViewModel = SearchViewModel(_dataService)
        searchViewModel.searchContext = _searchContext

        runBlocking {
            searchViewModel.searchById(id)
        }

        updateSearchResults(searchViewModel)
        return searchViewModel.hasSearchResults
    }

    private fun updateSearchResults(searchViewModel: SearchViewModel) {
        val searchRecyclerAdapter = SearchRecyclerAdapter { searchResult ->
            showItemDetail(searchResult, _dataService)
        }

        val lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if (lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        lstSearchResults.adapter = searchRecyclerAdapter
        searchRecyclerAdapter.setSearchContext(_searchContext)
        searchRecyclerAdapter.setSearchResults(searchViewModel.searchResults)
    }

    private fun getSearchContext(context: Context, attrs: AttributeSet): SearchContext? {
        val searchContext: SearchContext?
        val customAttributesStyle = context.obtainStyledAttributes(attrs, R.styleable.SearchResultComponent, 0, 0)
        try {
            val searchContextString = customAttributesStyle.getString(R.styleable.SearchResultComponent_searchContext)
            searchContext = SearchContext.values().find { it.name == searchContextString } ?: SearchContext.MOVIE
        } finally {
            customAttributesStyle.recycle()
        }
        return searchContext
    }

    private fun showItemDetail(model: ModelBase, dataService: IDataService) {
        val fragmentManager = (this.context as FragmentActivity).supportFragmentManager
        SearchResultDetailDialog(model, dataService).show(fragmentManager, "SearchResultDetailDialog")
    }
}

