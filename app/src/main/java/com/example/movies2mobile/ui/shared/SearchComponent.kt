package com.example.movies2mobile.ui.shared

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.data.DataService
import com.example.movies2mobile.models.MovieModel

class SearchComponent(context: Context, attrs: AttributeSet ) : ConstraintLayout(context, attrs) {

    var searchContext: SearchContext = SearchContext.MOVIE

    init {
        inflate(context, R.layout.search_component, this)

        var lstSearchResults = findViewById<RecyclerView>(R.id.lstSearchResults)
        if(lstSearchResults != null) {
            lstSearchResults.layoutManager = LinearLayoutManager(this.context)
        }

        var txtSearchText = findViewById<TextView>(R.id.txtSearch)
        var btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        var searchContext = getSearchContext(context, attrs)

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
        }
    }

    private fun getSearchContext(context: Context, attrs: AttributeSet): SearchContext? {
        var searchContext : SearchContext? = SearchContext.MOVIE
        val customAttributesStyle = context.obtainStyledAttributes(attrs, R.styleable.SearchComponent, 0, 0)
        try {
            var searchContextString = customAttributesStyle.getString(R.styleable.SearchComponent_searchContext)
            searchContext = SearchContext.values().find { it.name == searchContextString } ?: SearchContext.MOVIE
        } finally {
            customAttributesStyle.recycle()
            return searchContext
        }
    }

    private fun search(txtSearchText: TextView, lstSearchResults: RecyclerView, searchContext: SearchContext?) {

        var viewModel = SearchViewModel(DataService(this.context.filesDir.path))
        viewModel.searchText = txtSearchText.text.toString()
        viewModel.searchContext = searchContext
        viewModel.search()

        var searchRecyclerAdapter = SearchRecyclerAdapter { searchResult ->
            showItemDetail(searchResult)
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

    private fun showItemDetail(movieModel: MovieModel) {
        // show a simple alert dialog for now.  eg. https://www.tutorialkart.com/kotlin-android/android-alert-dialog-example/
        // TODO: use a custom layout with more detail. eg. https://developer.android.com/guide/topics/ui/dialogs
        val dialogBuilder = this.context?.let { AlertDialog.Builder(it) }

        dialogBuilder?.setMessage(movieModel.description)
            ?.setCancelable(false)
            ?.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder?.create()
        alert?.setTitle(movieModel.title)
        alert?.show()
    }

}