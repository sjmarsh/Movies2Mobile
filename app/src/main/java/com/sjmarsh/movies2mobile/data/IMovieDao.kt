package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import com.sjmarsh.movies2mobile.data.entities.SearchResultEntity

interface IMovieDao {
    suspend fun getAll(): List<MovieEntity>
    suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?, skip: Int, take: Int): SearchResultEntity<MovieEntity>
    suspend fun getMovieCategories(): List<String>
    suspend fun getMoviesByActorId(actorId: Int?): List<MovieEntity>
    suspend fun getMoviesByMovieId(movieId: Int?): List<MovieEntity>
}