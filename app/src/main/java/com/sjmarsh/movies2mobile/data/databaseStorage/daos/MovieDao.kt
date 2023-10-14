package com.sjmarsh.movies2mobile.data.databaseStorage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sjmarsh.movies2mobile.data.IMovieDao
import com.sjmarsh.movies2mobile.data.MovieSortBy
import com.sjmarsh.movies2mobile.data.entities.MovieEntity

@Dao
interface MovieDao : IMovieDao {

    @Query("SELECT * FROM movie")
    fun getAllFromDb(): List<MovieEntity>

    override suspend fun getAll(): List<MovieEntity> {
        return getAllFromDb()
    }
    @RawQuery
    fun searchMoviesFromDb(query: SupportSQLiteQuery) : List<MovieEntity>

    override suspend fun searchMovies(title: String?, category: String?, movieSortBy: MovieSortBy?): List<MovieEntity>{
        val safeTitle = if(title == null) "%%" else "%$title%"
        val safeCategory = category ?: ""
        val safeSortBy = movieSortBy?.toString()?.lowercase() ?: MovieSortBy.Title.toString().lowercase()
        val query = SimpleSQLiteQuery("SELECT * FROM movie WHERE title LIKE ? AND (? = '' OR Category = ?) ORDER BY $safeSortBy ASC", arrayOf(safeTitle, safeCategory, safeCategory))
        return searchMoviesFromDb(query)
    }

    @Query("SELECT DISTINCT category FROM movie ORDER BY category ASC")
    fun getMovieCategoriesFromDb(): List<String>
    override suspend fun getMovieCategories(): List<String> {
        return getMovieCategoriesFromDb()
    }

    @Query("SELECT m.* FROM movie m INNER JOIN movieActor ma ON m.id = ma.movieId WHERE ma.actorId = :actorId")
    fun getMoviesByActorIdFromDb(actorId: Int?): List<MovieEntity>

    override suspend fun getMoviesByActorId(actorId: Int?): List<MovieEntity>{
        return getMoviesByActorIdFromDb(actorId)
    }

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMoviesByMovieIdFromDb(movieId: Int?): List<MovieEntity>
    override suspend fun getMoviesByMovieId(movieId: Int?): List<MovieEntity>{
        return getMoviesByMovieIdFromDb(movieId)
    }

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