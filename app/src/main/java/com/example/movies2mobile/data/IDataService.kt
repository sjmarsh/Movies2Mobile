package com.example.movies2mobile.data

import com.example.movies2mobile.models.ActorModel
import com.example.movies2mobile.models.MovieModel

interface IDataService {
    fun searchMovies(title: String?, category: String?): List<MovieModel>
    fun searchActors(name: String?): List<ActorModel>
    fun getMovieCategories(): List<String>
    fun getActorsByIds(actorIds: List<Int?>): List<ActorModel>?
    fun getMoviesByActorId(actorId: Int?): List<MovieModel>?
}