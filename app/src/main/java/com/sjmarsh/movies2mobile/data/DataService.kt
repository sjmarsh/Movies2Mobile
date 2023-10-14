package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.data.mappers.toModel
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel

class DataService(private val dataStorage: IDataStorage) : IDataService {

    override suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?): List<MovieModel> {
        val movieEntities = dataStorage.movieDao().searchMovies(title, category, movieSortBy);
        return movieEntities.map { m -> m.toModel() }
    }

    override suspend fun searchActors(name: String?): List<ActorModel> {
        val actorEntities = dataStorage.actorDao().searchActors(name)
        return actorEntities.map { a -> a.toModel() }
    }

    private var categories: List<String>? = null
    override suspend fun getMovieCategories(): List<String> {
        if(categories == null) {
            categories = dataStorage.movieDao().getMovieCategories()
        }
        return categories as List<String>
    }

    override suspend fun getActorsById(actorId: Int?): List<ActorModel> {
        val actorEntities = dataStorage.actorDao().getActorsById(actorId)
        return actorEntities.map { a -> a.toModel() }
    }

    override suspend fun getActorsByMovieId(movieId: Int?): List<ActorModel> {
        val actorEntities = dataStorage.actorDao().getActorsByMovieId(movieId)
        return actorEntities.map { a -> a.toModel() }
    }

    override suspend fun getMoviesByActorId(actorId: Int?): List<MovieModel> {
        val movieEntities = dataStorage.movieDao().getMoviesByActorId(actorId)
        return movieEntities.map { m -> m.toModel() }
    }

    override suspend fun getMoviesByMovieId(movieId: Int?): List<MovieModel> {
        val movieEntities = dataStorage.movieDao().getMoviesByMovieId(movieId)
        return movieEntities.map { m -> m.toModel() }
    }
}
