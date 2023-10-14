package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.EntityBase

interface IActorDao {
    suspend fun getAll(): List<ActorEntity>
    suspend fun searchActors(name: String?): List<ActorEntity>
    suspend fun getActorsById(actorId: Int?): List<ActorEntity>
    suspend fun getActorsByMovieId(movieId: Int?): List<ActorEntity>
    suspend fun getActorIdsForMovie(movieId: Int?): List<Int>
}