package com.sjmarsh.movies2mobile.data.fileStorage.daos

import com.sjmarsh.movies2mobile.data.IMovieDao
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import com.sjmarsh.movies2mobile.data.fileStorage.MovieFile
import com.sjmarsh.movies2mobile.data.fileStorage.entities.MovieWithActorsEntity

class MovieDao(private val movieFile: MovieFile) : IMovieDao {
    override suspend fun getAll(): List<MovieEntity> {
        return movieFile.movies().map { m -> m.mapToMovieEntity() }
    }

    override suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?): List<MovieEntity> {
        val movies = getAll()
        var result: List<MovieEntity>? = if(title.isNullOrEmpty() && category.isNullOrEmpty()) {
            movies
        } else {
            movies.filter { m ->
                (title == null || m.title.contains(title, true))
                        && (category == null || category == "" || m.category == category)
            }.toList()
        }

        result = if(movieSortBy == null) {
            result?.sortedBy { m -> m.title }
        } else {
            getSortedResult(result, movieSortBy)
        }

        return result!!
    }

    override suspend fun getMovieCategories(): List<String> {
        val movies = getAll()
        val categories = movies.mapNotNull { m -> m.category }
        return categories.distinct().sorted()
    }

    override suspend fun getMoviesByActorId(actorId: Int?): List<MovieEntity> {
        val movies = movieFile.movies()
        val entities = movies.filter { m -> m.actors?.find { a -> a.id == actorId } != null }
        return entities.map { m -> m.mapToMovieEntity() }
    }

    override suspend fun getMoviesByMovieId(movieId: Int?): List<MovieEntity> {
        val movies = getAll()
        return movies.filter { m -> m.id == movieId }
    }

    private fun getSortedResult(result: List<MovieEntity>?, movieSortBy: MovieSortBy?): List<MovieEntity>? {
        if(result == null) return result

        return when (movieSortBy){
            MovieSortBy.Title -> result.sortedBy { m -> m.title }
            MovieSortBy.ReleaseYear -> result.sortedBy { m -> m.releaseYear }
            MovieSortBy.DateAdded -> result.sortedByDescending { m -> m.dateAdded }
            else -> result
        }
    }

    private fun MovieWithActorsEntity.mapToMovieEntity() : MovieEntity {
        return MovieEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            releaseYear = this.releaseYear,
            category = this.category,
            classification = this.classification,
            format = this.format,
            runningTime = this.runningTime,
            rating = this.rating,
            dateAdded = this.dateAdded,
            coverArt = this.coverArt
        )
    }
}