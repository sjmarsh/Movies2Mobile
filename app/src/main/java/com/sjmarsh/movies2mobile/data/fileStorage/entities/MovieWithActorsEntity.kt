package com.sjmarsh.movies2mobile.data.fileStorage.entities

import com.sjmarsh.movies2mobile.data.entities.EntityBase

class MovieWithActorsEntity (
    val id: Int,
    val title: String,
    val description: String?,
    val releaseYear: String?,
    val category: String?,
    val classification: String?,
    val format: String?,
    val runningTime: String?,
    val rating: Int?,
    val dateAdded: String?,
    val coverArt: String?,
    val actors: List<EntityBase>? = null
)