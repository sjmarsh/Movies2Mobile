package com.example.movies2mobile.ui.extensions

import android.annotation.SuppressLint
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("NewApi")
fun Date.toDisplayDate() : String? {
    val localDate = this?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
    return localDate?.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
}