package com.sjmarsh.movies2mobile.data

import android.util.Log
import com.sjmarsh.movies2mobile.data.fileStorage.entities.ImportEntity
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class JsonToModel : IJsonToModel {

    override fun convert(jsonString: String) : ImportEntity? {
        var model : ImportEntity? = null;
        try {
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
            val jsonAdapter: JsonAdapter<ImportEntity> = moshi.adapter(ImportEntity::class.java)

            model = jsonAdapter.fromJson(jsonString)
        }
        catch (e: Exception) {
            e.localizedMessage?.let { Log.e("JsonToModel", it) }
            when(e){
                is JsonDataException -> {
                    Log.i("JsonToModel", "Invalid json content in file")
                }
            }
        }
        return model
    }
}