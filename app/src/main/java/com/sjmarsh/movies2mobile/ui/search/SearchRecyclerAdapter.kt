package com.sjmarsh.movies2mobile.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.ModelBase
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.ui.extensions.toDisplayDate

class SearchRecyclerAdapter(private val onItemClicked: (ModelBase) -> Unit)
    : RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>(){

    private lateinit var _myRecyclerView: RecyclerView
    private var _searchContext: SearchContext? = SearchContext.MOVIE
    private var _searchResults: MutableList<ModelBase> = mutableListOf()

    fun setSearchContext(searchContext: SearchContext?) {
        _searchContext = searchContext
    }

    fun setSearchResults(searchResults: List<ModelBase>) {
        _searchResults = searchResults.toMutableList()
        super.notifyItemRangeChanged(0, _searchResults.count())
    }

    fun updateSearchResults(searchResults: List<ModelBase>) {
        if(!_searchResults.any()){
            setSearchResults(searchResults)
        }
        else {
            val newItemsStartPosition = _searchResults.count()
            val newItems = searchResults.filter { s -> _searchResults.firstOrNull { sr -> sr.id == s.id } == null }
            _searchResults.addAll(newItems)

            // need to use post to avoid updating during scroll event
            _myRecyclerView.post { super.notifyItemRangeInserted(newItemsStartPosition, newItems.count())}
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        _myRecyclerView = recyclerView
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

        fun bind(model: ModelBase) {
            // TODO - do this better (ie. adapter or provide pattern externally)
            if(model is MovieModel) {
                title.text = model.title
                detail.text = "${model.category} - ${model.releaseYear} - ${model.runningTime} - ${model.format}"
            }
            if(model is ActorModel) {
                title.text = model.fullName
                detail.text = "${model.sex} - ${model.dateOfBirth.toDisplayDate()}"
            }
        }

    }
}