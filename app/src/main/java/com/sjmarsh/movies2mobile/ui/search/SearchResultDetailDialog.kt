package com.sjmarsh.movies2mobile.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.sjmarsh.movies2mobile.R
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.models.ModelBase
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.ui.extensions.toDisplayDate
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sjmarsh.movies2mobile.models.ActorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SearchResultDetailDialog(private val model: ModelBase, private val dataService: IDataService): DialogFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // include this if round corners required
        //dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)

        val view = inflater.inflate(R.layout.dialog_search_result_detail, container, false)

        val txtDetailHeader = view.findViewById<TextView>(R.id.txtDetailHeader)
        val txtDetail = view.findViewById<TextView>(R.id.txtDetail)
        val txtDateAdded = view.findViewById<TextView>(R.id.txtDateAdded)
        val cgDetailItems = view.findViewById<ChipGroup>(R.id.cgDetailItems)
        val btnClose = view.findViewById<Button>(R.id.btnCloseMovieDetail)

        if(model is MovieModel){
            txtDetailHeader?.text = model.title
            txtDetail?.text = model.description
            txtDateAdded?.text = "Date Added: ${model.dateAdded.toDisplayDate()}"
            addActorSummaries(cgDetailItems, model)
        }
        if(model is ActorModel){
            txtDetailHeader?.text = model.fullName
            txtDetail?.text = "${model.sex} - ${model.dateOfBirth.toDisplayDate()}"
            addMovieSummaries(cgDetailItems, model)
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
// TODO  this should use it's own ViewModel rather than call data service directly!
    private fun addActorSummaries(cgDetailItems: ChipGroup, movieModel: MovieModel) {

        runBlocking {
            coroutineScope {
                val getActorsForMovieAsync = async(Dispatchers.IO) { dataService.getActorsByMovieId(movieModel.id) }

                withContext(Dispatchers.IO) {
                    val actors = getActorsForMovieAsync.await()
                    actors.forEach {
                        addChipToChipGroup(cgDetailItems, it.fullName, it.id, R.id.navigation_actors)
                    }
                }
            }
        }
    }

    private fun addMovieSummaries(cgDetailItems: ChipGroup, actorModel: ActorModel) {

        runBlocking {
            coroutineScope {
                val getMoviesByActorIdAsync = async(Dispatchers.IO) { dataService.getMoviesByActorId(actorModel.id) }
                withContext(Dispatchers.IO) {
                    val movies = getMoviesByActorIdAsync.await()
                    movies.forEach{
                        addChipToChipGroup(cgDetailItems, it.title, it.id, R.id.navigation_movies)
                    }
                }
            }
        }
    }

    private fun addChipToChipGroup(cgDetailItems: ChipGroup, chipTitle: String?, id: Int?, navigationResourceId: Int) {
        val chip = Chip(context)
        chip.text = chipTitle
        chip.setOnClickListener {
            dialog?.dismiss()
            val navOptions = NavOptions.Builder().setPopUpTo(navigationResourceId, false).build()
            findNavController().navigate(navigationResourceId, bundleOf("id" to id), navOptions)
        }
        cgDetailItems.addView(chip)
    }
}