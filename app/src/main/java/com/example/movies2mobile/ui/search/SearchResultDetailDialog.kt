package com.example.movies2mobile.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.movies2mobile.R
import com.example.movies2mobile.data.IDataService
import com.example.movies2mobile.models.ActorModel
import com.example.movies2mobile.models.ModelBase
import com.example.movies2mobile.models.MovieModel
import com.example.movies2mobile.ui.extensions.toDisplayDate
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchResultDetailDialog(model: ModelBase, dataService: IDataService): DialogFragment() {

    val _model = model
    val _dataService = dataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // include this if round corners required
        //dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)

        val view = inflater.inflate(R.layout.dialog_search_result_detail, container, false)

        val txtDetailHeader = view.findViewById<TextView>(R.id.txtDetailHeader)
        val txtDetail = view.findViewById<TextView>(R.id.txtDetail)
        val txtDateAdded = view.findViewById<TextView>(R.id.txtDateAdded)
        val cgDetailItems = view.findViewById<ChipGroup>(R.id.cgDetailItems)
        val btnClose = view.findViewById<Button>(R.id.btnCloseMovieDetail)

        if(_model is MovieModel){
            txtDetailHeader?.text = _model.title
            txtDetail?.text = _model.description
            txtDateAdded?.text = "Date Added: ${_model.dateAdded.toDisplayDate()}"
            addActorSummaries(cgDetailItems, _model)
        }
        if(_model is ActorModel){
            txtDetailHeader?.text = _model.fullName
            txtDetail?.text = "${_model.sex} - ${_model.dateOfBirth.toDisplayDate()}"
            addMovieSummaries(cgDetailItems, _model)
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

    private fun addActorSummaries(cgDetailItems: ChipGroup, movieModel: MovieModel) {
        if(movieModel.actors != null){
            val actors = _dataService.getActorsByIds(movieModel.actors.map { a -> a.id})
            actors?.forEach {
                addChipToChipGroup(cgDetailItems, it.fullName, it.id, R.id.navigation_actors)
            }
        }
    }

    private fun addMovieSummaries(cgDetailItems: ChipGroup, actorModel: ActorModel) {
        val movies = _dataService.getMoviesByActorId(actorModel.id)
        movies?.forEach{
            addChipToChipGroup(cgDetailItems, it.title, it.id, R.id.navigation_movies)
        }
    }

    private fun addChipToChipGroup(cgDetailItems: ChipGroup, chipTitle: String?, id: Int?, navigationResourceId: Int) {
        val chip = Chip(context)
        chip.text = chipTitle
        chip.setOnClickListener { _ ->
            dialog?.dismiss()
            findNavController().navigate(navigationResourceId, bundleOf("id" to id))
        }
        cgDetailItems.addView(chip)
    }
}