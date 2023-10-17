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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SearchResultComponent(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), KoinComponent
{
    private val _dataService by inject<IDataService>()
    private val _searchContext: SearchContext?
    private var _isLoadingResults: Boolean = false
    private var _searchViewModel: SearchViewModel
    private var _lstSearchResults: RecyclerView?

    init {
        inflate(context, R.layout.search_result_component, this)

        _lstSearchResults = findViewById(R.id.lstSearchResults)
        if(_lstSearchResults !== null) {
            _lstSearchResults!!.layoutManager = LinearLayoutManager(this.context)
            _lstSearchResults!!.adapter = SearchRecyclerAdapter { searchResult ->
                showItemDetail(searchResult, _dataService)
            }
            _lstSearchResults!!.recycledViewPool.clear()
        }
        _searchContext = getSearchContext(context, attrs)

        _searchViewModel = SearchViewModel(_dataService)

        _lstSearchResults!!.addOnScrollListener(object :
            PaginationScrollListener(_lstSearchResults!!.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                _isLoadingResults = true
                runBlocking {
                    coroutineScope {
                        val scrollSearchAsync = async(Dispatchers.IO) { _searchViewModel.scrollSearch() }
                        withContext(Dispatchers.IO) {
                            scrollSearchAsync.await()
                        }
                    }
                }
                updateSearchResults(_searchViewModel)
                _isLoadingResults = false
            }

            override fun isLastPage(): Boolean = _searchViewModel.isAllDataLoaded()
            override fun isLoading(): Boolean = _isLoadingResults;
        })

        search("")
    }

    fun search(query: String?, categoryFilter: String? = null, movieSortBy: MovieSortBy? = null) : Boolean {
        _searchViewModel.searchText = query
        _searchViewModel.categoryFilter = categoryFilter
        _searchViewModel.movieSortBy = movieSortBy
        _searchViewModel.searchContext = _searchContext

        runBlocking {
            coroutineScope {
                val searchAsync = async(Dispatchers.IO) { _searchViewModel.search() }
                withContext(Dispatchers.IO){
                    searchAsync.await()
                }
            }
        }

        setSearchResults(_searchViewModel)

        return _searchViewModel.hasSearchResults
    }

    fun searchById(id: Int?) : Boolean {
        _searchViewModel.searchContext = _searchContext

        runBlocking {
            coroutineScope {
                val searchAsync = async(Dispatchers.IO) { _searchViewModel.searchById(id) }
                withContext(Dispatchers.IO) {
                    searchAsync.await()
                }
            }
        }

        setSearchResults(_searchViewModel)
        return _searchViewModel.hasSearchResults
    }

    private fun setSearchResults(searchViewModel: SearchViewModel) {
        val searchRecyclerAdapter = _lstSearchResults!!.adapter as SearchRecyclerAdapter
        searchRecyclerAdapter.setSearchContext(_searchContext)
        searchRecyclerAdapter.setSearchResults(searchViewModel.searchResults)
    }

    private fun updateSearchResults(searchViewModel: SearchViewModel) {
        val searchRecyclerAdapter = _lstSearchResults!!.adapter as SearchRecyclerAdapter
        searchRecyclerAdapter.setSearchContext(_searchContext)
        searchRecyclerAdapter.updateSearchResults(searchViewModel.searchResults)
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

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

        if(!isLoading() && !isLastPage()) {
            if(visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}

