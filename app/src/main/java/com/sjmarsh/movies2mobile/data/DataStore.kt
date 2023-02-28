package com.sjmarsh.movies2mobile.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.ImportModel
import com.sjmarsh.movies2mobile.models.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class DataStore(context: Context) : IDataStore {

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
            return importData as ImportModel
        }

        var data: ImportModel? = null

        if(isExternalStorageAvailable) {
            val moviesFilePath = dataFilePath + "/${Constants.MOVIE_DATA_FILE}"
            val moviesFile = File(moviesFilePath)

            if (moviesFile.exists()) {
                try {
                    val kotlinModule: KotlinModule = KotlinModule.Builder()
                        .configure(KotlinFeature.StrictNullChecks, false)
                        .build()

                    val objectMapper: ObjectMapper = JsonMapper.builder()
                        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                        .addModule(kotlinModule)
                        .build()

                    val moviesJson = moviesFile.readText()

                    data = objectMapper.readValue(moviesJson)

                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.e("DataService", it) }
                    when(e){
                        // TODO - need to raise message in UI
                        is MismatchedInputException -> {
                            Log.i("DataService", "Invalid json content in file")
                        }
                        is IOException -> {
                            Log.i("DataService","File not found or could not be read")
                        }
                    }
                }
            }
        }

        return data?: ImportModel(listOf(), listOf())
    }

    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }
}