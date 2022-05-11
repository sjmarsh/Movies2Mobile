package com.example.movies2mobile.ui.concerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies2mobile.R
import com.example.movies2mobile.models.MovieModel


class ConcertRecyclerAdapter(
    private val onItemClicked: (MovieModel) -> Unit
): RecyclerView.Adapter<ConcertRecyclerAdapter.ViewHolder>() {

    private var concertList: List<MovieModel> = listOf() // todo add to constructor or public prop?

    public fun setConcerts(concerts: List<MovieModel>){
        concertList = concerts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_concert, parent, false)
        return ViewHolder(v){
            onItemClicked(concertList[it])
        }
    }

    override fun onBindViewHolder(holder: ConcertRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(concertList[position])
    }

    private fun getDetail(concertModel: MovieModel): String {
        return "${concertModel.releaseYear} - ${concertModel.runningTime} - ${concertModel.format}"
    }

    override fun getItemCount(): Int {
        return concertList.size
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

        fun bind(concertModel: MovieModel){
            title.setText(concertModel.title)
            detail.setText(getDetail(concertModel))
        }

        private fun getDetail(concertModel: MovieModel): String {
            return "${concertModel.releaseYear} - ${concertModel.runningTime} - ${concertModel.format}"
        }
    }
}