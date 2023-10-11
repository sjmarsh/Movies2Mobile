package com.sjmarsh.movies2mobile.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.ImportModel
import com.sjmarsh.movies2mobile.models.MovieModel
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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

                    val customDateAdapter: Any = object : Any() {
                        var dateFormat: DateFormat? = null
                        init {
                            dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
                            dateFormat!!.timeZone = TimeZone.getTimeZone("AEDT")
                        }
                        @ToJson
                        @Synchronized
                        fun dateToJson(d: Date?): String? {
                            return d?.let { dateFormat!!.format(it) }
                        }
                        @FromJson
                        @Synchronized
                        @Throws(ParseException::class)
                        fun dateToJson(s: String?): Date? {
                            return s?.let { dateFormat!!.parse(it) }
                        }
                    }

                    val moshi: Moshi = Moshi.Builder()
                        .add(customDateAdapter)
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                    val jsonAdapter: JsonAdapter<ImportModel> = moshi.adapter(ImportModel::class.java)

                    importData = jsonAdapter.fromJson(moviesJson)

                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.e("DataService", it) }
                    when(e){
                        // TODO - need to raise message in UI
                        is JsonDataException -> {
                            Log.i("DataService", "Invalid json content in file")
                        }
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