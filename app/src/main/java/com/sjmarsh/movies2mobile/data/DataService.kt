package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel

class DataService(private val dataStore: IDataStore) : IDataService {

    override suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?): List<MovieModel> {

        val movies = dataStore.movies()
        var result: List<MovieModel>? = if(title.isNullOrEmpty() && category.isNullOrEmpty()){
            movies
        } else {
            movies.filter { m ->
                (title == null || m.title!!.contains(title, true))
                        && (category == null || category == "" || m.category == category)
            }
                .toList()
        }

        result = if(movieSortBy == null) {
            result?.sortedBy { m -> m.title }
        } else {
            getSortedResult(result, movieSortBy)
        }

        return result!!
    }

    private fun getSortedResult(result: List<MovieModel>?, movieSortBy: MovieSortBy?): List<MovieModel>?{
        if(result == null) {
            return result
        }

        return when (movieSortBy){
            MovieSortBy.Title -> result.sortedBy { m -> m.title }
            MovieSortBy.ReleaseYear -> result.sortedBy { m -> m.releaseYear }
            MovieSortBy.DateAdded -> result.sortedByDescending { m -> m.dateAdded }
            else -> result
        }
    }

    override suspend fun searchActors(name: String?): List<ActorModel> {
        val actors = dataStore.actors()

        return if(name.isNullOrEmpty()){
            actors.sortedBy { a -> a.lastName }
        } else {
            actors.filter { a -> ((a.fullName != null && a.fullName.lowercase().contains(name.lowercase()))) }
                .toList()
                .sortedBy { a -> a.lastName }
        }
    }

    override suspend fun getMovieCategories(): List<String> {
        val movies = dataStore.movies()
        val categories = movies.mapNotNull { m -> m.category }
        return categories.distinct().sorted()
    }

    override suspend fun getActorsById(actorId: Int?): List<ActorModel> {
        val actors = dataStore.actors()
        return actors.filter { a -> a.id == actorId }
    }

    override suspend fun getActorsByIds(actorIds: List<Int?>): List<ActorModel> {
        val actors = dataStore.actors()
        return actors.filter { a -> actorIds.contains(a.id) }
    }

    override suspend fun getMoviesByActorId(actorId: Int?): List<MovieModel> {
        val movies = dataStore.movies()
        return movies.filter { m -> m.actors != null && m.actors.find { a -> a.id == actorId } != null }
    }

    override suspend fun getMoviesByMovieId(movieId: Int?): List<MovieModel> {
        val movies = dataStore.movies()
        return movies.filter { m -> m.id == movieId }
    }
}
