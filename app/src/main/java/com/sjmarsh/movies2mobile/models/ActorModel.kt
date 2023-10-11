package com.sjmarsh.movies2mobile.models

import com.squareup.moshi.Json
import java.util.Date

data class ActorModel(
    override val id: Int?,
    val firstName: String? = "",
    val lastName: String? = "",
    val sex: String? = "",
    @Json(ignore = true)
    val photo: String? = "",
    val fullName: String? = "",
    val dateOfBirth: Date? = null,
) : ModelBase(id)