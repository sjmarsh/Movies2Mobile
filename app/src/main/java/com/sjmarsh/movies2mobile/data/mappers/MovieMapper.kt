package com.sjmarsh.movies2mobile.data.mappers

import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.ui.extensions.toDate

fun MovieEntity.toModel() : MovieModel {
    return MovieModel(
        id = this.id,
        title = this.title,
        description = this.description,
        releaseYear = this.releaseYear,
        category = this.category,
        classification = this.classification,
        format = this.format,
        runningTime = this.runningTime,
        rating = this.rating,
        dateAdded = this.dateAdded.toDate(),
        coverArt = this.coverArt
    )
}
