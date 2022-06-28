package com.example.movies2mobile.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.movies2mobile.R
import com.example.movies2mobile.data.IDataService
import com.example.movies2mobile.models.MovieModel

class MovieDetailDialog(movieModel: MovieModel, dataService: IDataService): DialogFragment() {

    val _movieModel = movieModel
    val _dataService = dataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // include this if round corners required
        //dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)

        val view = inflater.inflate(R.layout.dialog_movie_detail, container, false)

        val txtDetailHeader = view.findViewById<TextView>(R.id.txtDetailHeader)
        val txtDetail = view.findViewById<TextView>(R.id.txtDetail)
        val txtActors = view.findViewById<TextView>(R.id.txtActors)
        val btnClose = view.findViewById<Button>(R.id.btnCloseMovieDetail)

        txtDetailHeader?.text = _movieModel.title
        txtDetail?.text = _movieModel.description
        txtActors.text = getActorSummary(_movieModel)

        btnClose.setOnClickListener {
            dialog?.dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun getActorSummary(movieModel: MovieModel) : String? {
        var actorSummary = ""
        if(movieModel.actors != null){
            var actors = _dataService.getActorsByIds(movieModel.actors.map { a -> a.id})
            actorSummary = actors?.joinToString { a -> "${a.firstName} ${a.lastName}" }.toString()
        }
        return actorSummary
    }
}