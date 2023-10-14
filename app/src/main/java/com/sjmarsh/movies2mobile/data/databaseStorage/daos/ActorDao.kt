package com.sjmarsh.movies2mobile.data.databaseStorage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.EntityBase

@Dao
interface ActorDao : IActorDao {
    @Query("SELECT * FROM actor")
    fun getAllFromDb() : List<ActorEntity>
    override suspend fun getAll() : List<ActorEntity>{
        return getAllFromDb()
    }

    @Query("SELECT * FROM actor WHERE fullName Like :name ORDER BY lastName ASC")
    fun searchActorsFromDb(name: String?): List<ActorEntity>
    override suspend fun searchActors(name: String?): List<ActorEntity>{
        val safeName = if(name == null) "%%" else "%$name%"
        return searchActorsFromDb(safeName)
    }

    @Query("SELECT * FROM actor WHERE id = :actorId")
    fun getActorsByIdFromDb(actorId: Int?): List<ActorEntity>
    override suspend fun getActorsById(actorId: Int?): List<ActorEntity>{
        return getActorsByIdFromDb(actorId)
    }

    @Query("SELECT a.* FROM actor a INNER JOIN movieActor ma ON a.id = ma.actorId WHERE ma.movieId = :movieId")
    fun getActorsByMovieIdFromDb(movieId: Int?): List<ActorEntity>

    override suspend fun getActorsByMovieId(movieId: Int?): List<ActorEntity>{
        return getActorsByMovieIdFromDb(movieId)
    }

    @Query("SELECT actorId FROM movieActor WHERE movieId = :movieId")
    fun getActorIdsForMovieFromDb(movieId: Int?): List<Int>
    override suspend fun getActorIdsForMovie(movieId: Int?): List<Int> {
        return getActorIdsForMovieFromDb(movieId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg actor: ActorEntity)

    @Insert
    fun insertAll(actors: List<ActorEntity>)

    @Delete
    fun delete(actor: ActorEntity)

    @Query("DELETE FROM actor")
    fun deleteAll()

    @Transaction
    open fun initActors(actors: List<ActorEntity>){
        deleteAll()
        insertAll(actors)
    }
}