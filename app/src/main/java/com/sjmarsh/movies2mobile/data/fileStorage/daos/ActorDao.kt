package com.sjmarsh.movies2mobile.data.fileStorage.daos

import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.SearchResultEntity
import com.sjmarsh.movies2mobile.data.fileStorage.MovieFile

class ActorDao(private val movieFile: MovieFile) : IActorDao {

    override suspend fun getAll(): List<ActorEntity> {
        return movieFile.actors()
    }

    override suspend fun searchActors(name: String?, skip: Int, take: Int): SearchResultEntity<ActorEntity> {
        val actors = getAll()
        val result = if(name.isNullOrEmpty()){
            actors.sortedBy { a -> a.lastName }
        } else {
            actors.filter { a -> ((a.fullName != null && a.fullName.lowercase().contains(name.lowercase()))) }
                .toList()
                .sortedBy { a -> a.lastName }
        }
        val count = result!!.size
        return SearchResultEntity(result.subList(skip, take), count)
    }

    override suspend fun getActorsById(actorId: Int?): List<ActorEntity> {
        val actors = getAll()
        return actors.filter { a -> a.id == actorId }
    }

    override suspend fun getActorsByMovieId(movieId: Int?): List<ActorEntity> {
        val movie = movieFile.movies().firstOrNull{ m -> m.id == movieId }
        if(movie?.actors == null) return listOf()
        return getActorsByIds(movie.actors.map{ a -> a.id})
    }

    private suspend fun getActorsByIds(actorIds: List<Int?>): List<ActorEntity> {
        val actors = getAll()
        return actors.filter { a -> actorIds.contains(a.id) }
    }

    override suspend fun getActorIdsForMovie(movieId: Int?): List<Int> {
        val ids = listOf<Int>()
        if(movieId == null) return ids
        val movie = movieFile.movies().firstOrNull{ m -> m.id == movieId}
        if(movie?.actors == null) return ids
        return  movie.actors.map{ a -> a.id }
    }
}