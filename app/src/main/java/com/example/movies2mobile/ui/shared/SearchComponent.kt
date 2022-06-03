package com.example.movies2mobile.ui.shared

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel
import com.example.movies2mobile.ui.extensions.setOnRightDrawableClicked

class SearchComponent(context: Context, attrs: AttributeSet ) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.search_component, this)

        val lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if(lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        val txtSearchText = findViewById<TextView>(R.id.txtSearch)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        val searchContext = getSearchContext(context, attrs)

        if(txtSearchText != null && btnSearch != null){

            search(txtSearchText, lstSearchResults, searchContext)

            btnSearch.setOnClickListener {
                search(txtSearchText, lstSearchResults, searchContext)
            }

            txtSearchText.setOnEditorActionListener { _, actionId, _ ->
                when(actionId){
                    EditorInfo.IME_ACTION_SEARCH -> {
                        search(txtSearchText, lstSearchResults, searchContext)
                        true
                    }
                    else -> false
                }
            }

            //clear search
            txtSearchText.setOnRightDrawableClicked {
                it.text = ""
                search(txtSearchText, lstSearchResults, searchContext)
            }
        }
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

    private fun search(txtSearchText: TextView, lstSearchResults: RecyclerView, searchContext: SearchContext?) {
        val dataService = DataService(this.context.filesDir.path)
        val viewModel = SearchViewModel(dataService)
        viewModel.searchText = txtSearchText.text.toString()
        viewModel.searchContext = searchContext
        viewModel.search()

        val searchRecyclerAdapter = SearchRecyclerAdapter { searchResult ->
            showItemDetail(searchResult, dataService)
        }

        lstSearchResults.adapter = searchRecyclerAdapter
        searchRecyclerAdapter.setSearchContext(searchContext)
        searchRecyclerAdapter.setSearchResults(viewModel.searchResults)

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showItemDetail(movieModel: MovieModel, dataService: DataService) {
        val fragmentManager = (this.context as FragmentActivity).supportFragmentManager
        MovieDetailDialog(movieModel, dataService).show(fragmentManager, "MovieDetailDialog")
    }
}

