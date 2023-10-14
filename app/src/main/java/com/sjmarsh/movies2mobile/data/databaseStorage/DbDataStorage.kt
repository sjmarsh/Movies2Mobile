package com.sjmarsh.movies2mobile.data.databaseStorage

import android.content.Context
import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.IDataStorage
import com.sjmarsh.movies2mobile.data.IMovieDao

class DbDataStorage(context: Context) : IDataStorage {

    private val _context = context
    private val database : MovieDatabase

    init {
        database = MovieDatabase(context)
    }
    override fun movieDao(): IMovieDao {
        return database.movieDao()
    }

    override fun actorDao(): IActorDao {
        return database.actorDao()
    }
}