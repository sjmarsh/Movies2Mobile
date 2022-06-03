package com.example.movies2mobile.models

import com.fasterxml.jackson.annotation.JsonIgnore

class MovieModel (
    id: Int?,
    val title: String?,
    val description: String?,
    val releaseYear: String?,
    val category: String?,
    val classification: String?,
    val format: String?,
    val runningTime: String?,
    val rating: Int?,
    @JsonIgnore
    val coverArt: String?,
    val actors: List<ModelBase>?
) : ModelBase(id)