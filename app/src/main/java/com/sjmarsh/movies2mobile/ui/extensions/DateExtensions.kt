package com.sjmarsh.movies2mobile.ui.extensions

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

const val DISPLAY_DATE_PATTERN: String = "dd-MMM-yyyy"
const val DATABASE_DATE_PATTERN: String = "yyyy-MM-dd"
const val TIME_ZONE: String = "AEDT"

@SuppressLint("NewApi")
fun Date?.toDisplayDate() : String? {
    if(this == null) return ""
    val localDate = this.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
    return localDate?.format(DateTimeFormatter.ofPattern(DISPLAY_DATE_PATTERN))
}

@SuppressLint("NewApi")
fun Date?.toDatabaseDate() : String? {
    if(this == null) return ""
    val localDate = this.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
    return localDate?.format(DateTimeFormatter.ofPattern(DATABASE_DATE_PATTERN))
}

@SuppressLint("SimpleDateFormat")
fun String?.toDate() : Date? {
    if(this == null) return  null

    try {
        val formatter = SimpleDateFormat(DATABASE_DATE_PATTERN)
        formatter.timeZone = TimeZone.getTimeZone(TIME_ZONE)
        return formatter.parse(this)
    } catch (e: Exception) {
        e.localizedMessage?.let { Log.e("DateExtensions", it) }
        when(e){
            is ParseException -> {
                Log.i("DateExtensions", "Unable to parse date: $this")
            }
        }
    }
    return null
}