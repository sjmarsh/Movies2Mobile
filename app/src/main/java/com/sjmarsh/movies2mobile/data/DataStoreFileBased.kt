package com.sjmarsh.movies2mobile.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.ImportModel
import com.sjmarsh.movies2mobile.models.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class DataStoreFileBased(context: Context, jsonToModel: IJsonToModel) : IDataStore {

    private val _jsonToModel : IJsonToModel = jsonToModel
    private var dataFilePath: String = ""
    private var importData: ImportModel? = null

    init {
        dataFilePath = context.filesDir.path
    }

    override suspend fun movies() : List<MovieModel>{
        val allData = loadAllDataAsync()
        var movies = allData.movies
        if(movies == null) {
            movies = emptyList()
        }
        return movies
    }

    override suspend fun actors() : List<ActorModel> {
        val allData = loadAllDataAsync()
        var actors = allData.actors
        if(actors == null) {
            actors = emptyList()
        }
        return actors
    }

    private suspend fun loadAllDataAsync() : ImportModel = withContext(Dispatchers.IO) {
        loadAllData()
    }

    private fun loadAllData(): ImportModel {

        if(importData !== null){
            Log.i("DataStore", "Using cached data")
            return importData as ImportModel
        }

        Log.i("DataStore", "Loading data from file")

        if(isExternalStorageAvailable) {
            val moviesFilePath = dataFilePath + "/${Constants.MOVIE_DATA_FILE}"
            val moviesFile = File(moviesFilePath)

            if (moviesFile.exists()) {
                try {
                    val moviesJson = moviesFile.readText()
                    importData = _jsonToModel.convert(moviesJson)

                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.e("DataService", it) }
                    when(e){
                        // TODO - need to raise message in UI
                        is IOException -> {
                            Log.i("DataService","File not found or could not be read")
                        }
                    }
                }
            }
        }

        return importData?: ImportModel(listOf(), listOf())
    }

    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }
}