package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.models.SearchResult

interface IDataService {
    suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?, skip: Int, take: Int): SearchResult<MovieModel>
    suspend fun searchActors(name: String?, skip: Int, take: Int): SearchResult<ActorModel>
    suspend fun getMovieCategories(): List<String>
    suspend fun getActorsById(actorId: Int?): List<ActorModel>
    suspend fun getActorsByMovieId(movieId: Int?): List<ActorModel>
    suspend fun getMoviesByActorId(actorId: Int?): List<MovieModel>
    suspend fun getMoviesByMovieId(movieId: Int?): List<MovieModel>
}