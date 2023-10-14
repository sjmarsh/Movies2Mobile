package com.sjmarsh.movies2mobile.data.fileStorage

import android.content.Context
import android.os.Environment
import android.util.Log
import com.sjmarsh.movies2mobile.data.JsonToModel
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.fileStorage.entities.ImportEntity
import com.sjmarsh.movies2mobile.data.fileStorage.entities.MovieWithActorsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MovieFile(context: Context) {

    private val _jsonToModel = JsonToModel()
    private var dataFilePath: String = ""
    private var importData: ImportEntity? = null

    init {
        dataFilePath = context.filesDir.path
    }

    suspend fun movies() : List<MovieWithActorsEntity>{
        val allData = loadAllDataAsync()
        var movies = allData.movies
        if(movies == null) {
            movies = emptyList()
        }
        return movies
    }

    suspend fun actors() : List<ActorEntity> {
        val allData = loadAllDataAsync()
        var actors = allData.actors
        if(actors == null) {
            actors = emptyList()
        }
        return actors
    }

    private suspend fun loadAllDataAsync() : ImportEntity = withContext(Dispatchers.IO) {
        loadAllData()
    }

    private fun loadAllData(): ImportEntity {

        if(importData !== null){
            Log.i("MovieFile", "Using cached data")
            return importData as ImportEntity
        }

        Log.i("MovieFile", "Loading data from file")

        if(isExternalStorageAvailable) {
            val moviesFilePath = dataFilePath + "/${Constants.MOVIE_DATA_FILE}"
            val moviesFile = File(moviesFilePath)

            if (moviesFile.exists()) {
                try {
                    val moviesJson = moviesFile.readText()
                    importData = _jsonToModel.convert(moviesJson)

                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.e("MovieFile", it) }
                    when(e){
                        // TODO - need to raise message in UI
                        is IOException -> {
                            Log.i("MovieFile","File not found or could not be read")
                        }
                    }
                }
            }
        }

        return importData?: ImportEntity(listOf(), listOf())
    }

    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }
}