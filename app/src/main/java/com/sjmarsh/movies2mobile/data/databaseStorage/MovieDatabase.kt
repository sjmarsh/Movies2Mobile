package com.sjmarsh.movies2mobile.data.databaseStorage

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sjmarsh.movies2mobile.data.databaseStorage.daos.ActorDao
import com.sjmarsh.movies2mobile.data.databaseStorage.daos.MovieActorDao
import com.sjmarsh.movies2mobile.data.databaseStorage.daos.MovieDao
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(entities = [MovieEntity::class, ActorEntity::class, MovieActorEntity::class], version = 2, exportSchema = true)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun actorDao() : ActorDao
    abstract fun movieActorDao() : MovieActorDao

    companion object {
        @Volatile private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            MovieDatabase::class.java, "movieDb.db")
            .fallbackToDestructiveMigration()
            .setQueryCallback(object : QueryCallback {
                override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                    Log.i("MovieDatabase", "Sql Query: $sqlQuery SqlArgs: $bindArgs")
                }
            }, Executors.newSingleThreadExecutor() )
            .build()
    }
}