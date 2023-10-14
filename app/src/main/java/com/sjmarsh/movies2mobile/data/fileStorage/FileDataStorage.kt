package com.sjmarsh.movies2mobile.data.fileStorage

import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.IDataStorage
import com.sjmarsh.movies2mobile.data.IMovieDao
import com.sjmarsh.movies2mobile.data.fileStorage.daos.ActorDao
import com.sjmarsh.movies2mobile.data.fileStorage.daos.MovieDao

class FileDataStorage(private val movieFile: MovieFile) : IDataStorage {

    override fun movieDao(): IMovieDao {
        return MovieDao(movieFile)
    }

    override fun actorDao(): IActorDao {
        return ActorDao(movieFile)
    }

}