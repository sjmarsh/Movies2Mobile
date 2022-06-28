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
import com.example.movies2mobile.models.ActorModel
import com.example.movies2mobile.models.ModelBase
import com.example.movies2mobile.models.MovieModel
import com.example.movies2mobile.ui.extensions.toDisplayDate

class SearchResultDetailDialog(model: ModelBase, dataService: IDataService): DialogFragment() {

    val _model = model
    val _dataService = dataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // include this if round corners required
        //dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)

        val view = inflater.inflate(R.layout.dialog_search_result_detail, container, false)

        val txtDetailHeader = view.findViewById<TextView>(R.id.txtDetailHeader)
        val txtDetail = view.findViewById<TextView>(R.id.txtDetail)
        val txtDetailItems = view.findViewById<TextView>(R.id.txtDetailItems)
        val btnClose = view.findViewById<Button>(R.id.btnCloseMovieDetail)

        if(_model is MovieModel){
            txtDetailHeader?.text = _model.title
            txtDetail?.text = _model.description
            txtDetailItems.text = getActorSummary(_model)
        }
        if(_model is ActorModel){
            txtDetailHeader?.text = _model.fullName
            txtDetail?.text = "${_model.sex} - ${_model.dateOfBirth?.toDisplayDate()}"
            txtDetailItems.text = getMovieSummary(_model)
        }

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

    private fun getActorSummary(movieModel: MovieModel) : String {
        var actorSummary = ""
        if(movieModel.actors != null){
            val actors = _dataService.getActorsByIds(movieModel.actors.map { a -> a.id})
            actorSummary = actors?.joinToString { a -> "${a.firstName} ${a.lastName}" }.toString()
        }
        return actorSummary
    }

    private fun getMovieSummary(actorModel: ActorModel) : String {
        val movies = _dataService.getMoviesByActorId(actorModel.id)
        return movies?.joinToString { m -> "${m.title}" }.toString()
    }
}