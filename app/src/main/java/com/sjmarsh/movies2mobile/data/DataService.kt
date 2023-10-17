package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.data.mappers.toModel
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.models.SearchResult

class DataService(private val dataStorage: IDataStorage) : IDataService {

    override suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?, skip: Int, take: Int): SearchResult<MovieModel> {
        val searchResultEntity = dataStorage.movieDao().searchMovies(title, category, movieSortBy, skip, take);
        return SearchResult(
            searchResultEntity.results.map { m -> m.toModel() },
            searchResultEntity.totalCount
        )
    }

    override suspend fun searchActors(name: String?, skip: Int, take: Int): SearchResult<ActorModel> {
        val searchResultEntity = dataStorage.actorDao().searchActors(name, skip, take)
        return SearchResult(
            searchResultEntity.results.map { a -> a.toModel()},
            searchResultEntity.totalCount
        )
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
