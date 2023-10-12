package com.sjmarsh.movies2mobile.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sjmarsh.movies2mobile.data.Constants
import com.sjmarsh.movies2mobile.data.IJsonToModel
import com.sjmarsh.movies2mobile.data.MovieDatabase
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import com.sjmarsh.movies2mobile.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class SettingsFragment() : Fragment() {

    private val _jsonToModel : IJsonToModel by inject()

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnSelectMovieDataFile: Button = binding.btnSelectMovieDataFile
        btnSelectMovieDataFile.setOnClickListener {
            selectMovieFile.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                    "text/json", // .json
                    "text/plain" // .txt
                ))
            })
        }

        return root
    }

    private val selectMovieFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleSelectMovieFileResult(result)
    }

    private fun handleSelectMovieFileResult(result: ActivityResult) {
        val data = result.data
        val uri = data?.data

        try {
            val jsonString = getMovieFileContentJson(uri)
            writeToLocalAppFileStorage(jsonString)
            writeToLocalAppDatabase(jsonString)
        } catch (e: IOException) {
            e.localizedMessage?.let { Log.e("Select Movie Data", it) }
            Toast.makeText(this.context, "Fail to read or write file", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getMovieFileContentJson(uri: Uri?): String {
        val inputStream: InputStream = activity?.contentResolver?.openInputStream(uri!!)
            ?: throw Exception("The input stream was null")
        val size: Int = inputStream.available()
        val bytes = ByteArray(size)
        inputStream.read(bytes)
        inputStream.close()
        val jsonString = String(bytes, StandardCharsets.UTF_8)
        return jsonString
    }

    private fun writeToLocalAppFileStorage(jsonString: String) {
        val targetFilePath = this.context?.filesDir?.path + "/${Constants.MOVIE_DATA_FILE}"
        File(targetFilePath).writeText(jsonString)

        Toast.makeText(this.context, "Movies data imported", Toast.LENGTH_SHORT)
            .show()
    }

    private fun writeToLocalAppDatabase(jsonString: String) {

        val importModel = _jsonToModel.convert(jsonString)
        if(importModel !== null) {
            val db = this.context?.let { MovieDatabase(it) } // TODO try to inject this
            lifecycleScope.launch(Dispatchers.IO) {
                db?.runInTransaction {
                    run {
                        if (importModel.movies !== null) {
                            val movieEntities = importModel.movies!!.map { m ->
                                MovieEntity(
                                    m.id,
                                    m.title,
                                    m.description,
                                    m.releaseYear,
                                    m.category,
                                    m.classification,
                                    m.format,
                                    m.runningTime,
                                    m.rating,
                                    m.dateAdded.toString(),
                                    m.coverArt
                                )
                            }
                            db.movieDao().initMovies(movieEntities)
                        }
                        if (importModel.actors !== null) {
                            val actorEntities = importModel.actors!!.map { a ->
                                ActorEntity(
                                    a.id,
                                    a.firstName,
                                    a.lastName,
                                    a.sex,
                                    a.photo,
                                    a.fullName,
                                    a.dateOfBirth.toString()
                                )
                            }
                            db.actorDao().initActors(actorEntities)
                        }
                    }
                }
            }
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}