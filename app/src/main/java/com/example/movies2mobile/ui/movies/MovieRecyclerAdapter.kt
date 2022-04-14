package com.example.movies2mobile.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.models.MovieModel

public class MovieRecyclerAdapter(
    private val onItemClicked: (MovieModel) -> Unit
): RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder>() {

    private var movieList: List<MovieModel> = listOf() // todo add to constructor or public prop?

    public fun setMovies(movies: List<MovieModel>){
        movieList = movies
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_movie, parent, false)
        return ViewHolder(v){
            onItemClicked(movieList[it])
        }
    }

    override fun onBindViewHolder(holder: MovieRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }


    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class ViewHolder(itemView: View, onItemClicked: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var detail: TextView

        init {
            title = itemView.findViewById(R.id.title)
            detail = itemView.findViewById(R.id.detail)

            itemView.setOnClickListener {
                onItemClicked(absoluteAdapterPosition)
            }
        }

        fun bind(movieModel: MovieModel){
            title.setText(movieModel.title)
            detail.setText(getDetail(movieModel))
        }

        private fun getDetail(movieModel: MovieModel): String {
            return "${movieModel.category} - ${movieModel.releaseYear} - ${movieModel.runningTime} - ${movieModel.format}"
        }
    }
}