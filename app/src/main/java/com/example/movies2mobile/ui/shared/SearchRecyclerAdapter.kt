package com.example.movies2mobile.ui.shared

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.models.MovieModel

class SearchRecyclerAdapter(private val onItemClicked: (MovieModel) -> Unit)
    : RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>(){

    private var _searchContext: SearchContext? = SearchContext.MOVIE
    private var _searchResults: List<MovieModel> = listOf() // todo add to constructor or public prop?

    fun setSearchContext(searchContext: SearchContext?) {
        _searchContext = searchContext
    }

    fun setSearchResults(searchResults: List<MovieModel>) {
        _searchResults = searchResults
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecyclerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_search_result, parent, false)
        return ViewHolder(itemView) { onItemClicked(_searchResults[it]) }
    }

    override fun onBindViewHolder(holder: SearchRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(_searchResults[position])
    }

    override fun getItemCount(): Int {
        return _searchResults.size
    }

    inner class ViewHolder(itemView: View, onItemClicked: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        private var title: TextView
        private var detail: TextView

        init {
            title = itemView.findViewById(R.id.title)
            detail = itemView.findViewById(R.id.detail)

            itemView.setOnClickListener { onItemClicked(absoluteAdapterPosition) }
        }

        fun bind(movieModel: MovieModel) {
            title.setText(movieModel.title)
            detail.setText(getDetail(movieModel, _searchContext))
        }

        private fun getDetail(movieModel: MovieModel, searchContext: SearchContext?): String {
            when(searchContext) {
                SearchContext.MOVIE -> return "${movieModel.category} - ${movieModel.releaseYear} - ${movieModel.runningTime} - ${movieModel.format}"
                SearchContext.CONCERT -> return "${movieModel.releaseYear} - ${movieModel.runningTime} - ${movieModel.format}"
                else -> { Log.e("SearchRecycler","Search Context not supported")}
            }
            return ""
        }
    }
}