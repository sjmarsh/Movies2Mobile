package com.sjmarsh.movies2mobile.data.fileStorage.entities

import com.sjmarsh.movies2mobile.data.entities.ActorEntity

data class ImportEntity (
    var movies: List<MovieWithActorsEntity>?,
    var actors: List<ActorEntity>?
)