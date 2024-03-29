package com.sjmarsh.movies2mobile.models

import com.squareup.moshi.Json
import java.util.Date

class MovieModel (
    override val id: Int,
    val title: String = "",
    val description: String? = "",
    val releaseYear: String? = "",
    val category: String? = "",
    val classification: String? = "",
    val format: String? = "",
    val runningTime: String? = "",
    val rating: Int? = 0,
    val dateAdded: Date? = null,
    @Json(ignore = true)
    val coverArt: String? = ""
) : ModelBase(id)