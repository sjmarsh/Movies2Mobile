package com.example.movies2mobile.data

import android.os.Environment
import android.util.Log
import com.example.movies2mobile.models.ActorModel
import com.example.movies2mobile.models.ImportModel
import com.example.movies2mobile.models.MovieModel
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.IOException

class DataService(private val dataFilePath: String) {

    private var importData: ImportModel? = null

    init {
        if(importData == null){
            importData = getAllData()
        }
    }

    fun searchMovies(title: String?, category: String?): List<MovieModel> {
        if(title.isNullOrEmpty() && category.isNullOrEmpty()){
            return importData?.movies?.sortedBy { m -> m.title } ?: listOf()
        }

        return importData?.movies?.filter {
                m -> (title == null || m.title!!.contains(title, true))
                    && (category == null || category == "" || m.category == category)
        }
            ?.toList()
            ?.sortedBy { m -> m.title }
            ?: listOf()
    }

    fun searchConcerts(title: String?): List<MovieModel> {
        if(title.isNullOrEmpty()){
            return importData?.concerts?.sortedBy { m -> m.title } ?: listOf()
        }

        return importData?.concerts?.filter { m -> m.title == null || m.title.contains(title, true) }?.toList()
            ?.sortedBy { m -> m.title }
            ?: listOf()
    }

    fun getMovieCategories(): List<String> {
        val categories = importData?.movies?.mapNotNull { m -> m.category }
        return categories?.distinct()?.sorted() ?: listOf()
    }

    fun getActorsByIds(actorIds: List<Int?>): List<ActorModel>? {
        return importData?.actors?.filter { a -> actorIds.contains(a.id) }
    }

    private fun getAllData(): ImportModel {

        var data: ImportModel? = null

        if(isExternalStorageAvailable) {
            val moviesFilePath = dataFilePath + "/${Constants.MOVIE_DATA_FILE}"
            val moviesFile = File(moviesFilePath)

            if (moviesFile.exists()) {
                try {
                    val objectMapper = jacksonObjectMapper()
                    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    val moviesJson = moviesFile.readText()
                    data = objectMapper.readValue(moviesJson)
                } catch (e: Exception) {
                    Log.e("DataService", e.localizedMessage)
                    when(e){
                        // TODO - need to raise message in UI
                        is IOException -> {
                            Log.i("DataService","File not found or could not be read")
                        }
                        is MismatchedInputException -> {
                            Log.i("DataService", "Invalid json content in file")
                        }
                    }
                }
            }
        }

        return data?: ImportModel(listOf(), listOf(), listOf())
    }

    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

}
