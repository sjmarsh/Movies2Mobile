package com.example.movies2mobile.ui.shared

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel

class SearchComponent(context: Context, attrs: AttributeSet ) : ConstraintLayout(context, attrs) {

    private val _searchContext: SearchContext?

    init {
        inflate(context, R.layout.search_component, this)

        val lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if(lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        _searchContext = getSearchContext(context, attrs)

        search("")
    }

    fun search(query: String?, categoryFilter: String? = null) : Boolean {

        val dataService = DataService(this.context.filesDir.path) // TODO dependency injection

        val searchViewModel = SearchViewModel(dataService)
        searchViewModel.searchText = query
        searchViewModel.categoryFilter = categoryFilter
        searchViewModel.searchContext = _searchContext
        searchViewModel.search()

        val searchRecyclerAdapter = SearchRecyclerAdapter { searchResult ->
            showItemDetail(searchResult, dataService)
        }

        val lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if(lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        lstSearchResults.adapter = searchRecyclerAdapter
        searchRecyclerAdapter.setSearchContext(_searchContext)
        searchRecyclerAdapter.setSearchResults(searchViewModel.searchResults)
        return searchViewModel.hasSearchResults
    }

    private fun getSearchContext(context: Context, attrs: AttributeSet): SearchContext? {
        val searchContext: SearchContext?
        val customAttributesStyle = context.obtainStyledAttributes(attrs, R.styleable.SearchComponent, 0, 0)
        try {
            val searchContextString = customAttributesStyle.getString(R.styleable.SearchComponent_searchContext)
            searchContext = SearchContext.values().find { it.name == searchContextString } ?: SearchContext.MOVIE
        } finally {
            customAttributesStyle.recycle()
        }
        return searchContext
    }

    private fun showItemDetail(movieModel: MovieModel, dataService: DataService) {
        val fragmentManager = (this.context as FragmentActivity).supportFragmentManager
        MovieDetailDialog(movieModel, dataService).show(fragmentManager, "MovieDetailDialog")
    }
}

