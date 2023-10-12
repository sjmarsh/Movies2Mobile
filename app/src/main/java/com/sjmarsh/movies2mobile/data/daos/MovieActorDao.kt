package com.sjmarsh.movies2mobile.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sjmarsh.movies2mobile.data.entities.MovieActorEntity

@Dao
interface MovieActorDao {

    @Query("SELECT * FROM movieActor WHERE movieId = :movieId")
    fun getByMovieId(movieId: Int) : List<MovieActorEntity>

    @Insert
    fun insertAll(movieActors: List<MovieActorEntity>)

    @Query("DELETE FROM movieActor")
    fun deleteAll()

    @Transaction
    open fun initMovieActors(movieActors: List<MovieActorEntity>){
        deleteAll()
        insertAll(movieActors)
    }
}