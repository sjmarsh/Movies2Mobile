package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel

interface IDataStore {
    suspend fun movies() : List<MovieModel>
    suspend fun actors() : List<ActorModel>
}