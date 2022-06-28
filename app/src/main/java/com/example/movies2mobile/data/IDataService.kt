package com.example.movies2mobile.data

import com.example.movies2mobile.models.ActorModel
import com.example.movies2mobile.models.MovieModel

interface IDataService {
    fun searchMovies(title: String?, category: String?): List<MovieModel>
    fun searchConcerts(title: String?): List<MovieModel>
    fun getMovieCategories(): List<String>
    fun getActorsByIds(actorIds: List<Int?>): List<ActorModel>?
}