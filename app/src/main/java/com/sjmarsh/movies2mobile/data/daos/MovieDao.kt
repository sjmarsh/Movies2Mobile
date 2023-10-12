package com.sjmarsh.movies2mobile.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sjmarsh.movies2mobile.data.entities.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll() : List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: MovieEntity)

    @Insert
    fun insertAll(movies: List<MovieEntity>)

    @Delete
    fun delete(movie: MovieEntity)

    @Query("DELETE FROM movie")
    fun deleteAll()

    @Transaction
    open fun initMovies(movies: List<MovieEntity>) {
        deleteAll()
        insertAll(movies)
    }
}