package com.sjmarsh.movies2mobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sjmarsh.movies2mobile.data.daos.ActorDao
import com.sjmarsh.movies2mobile.data.daos.MovieDao
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieEntity

@Database(entities = [MovieEntity::class, ActorEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun actorDao() : ActorDao

    companion object {
        @Volatile private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            MovieDatabase::class.java, "movieDb.db").build()
    }
}