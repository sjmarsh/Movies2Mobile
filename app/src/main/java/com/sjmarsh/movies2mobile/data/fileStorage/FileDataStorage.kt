package com.sjmarsh.movies2mobile.data.fileStorage

import android.content.Context
import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.IDataStorage
import com.sjmarsh.movies2mobile.data.IMovieDao
import com.sjmarsh.movies2mobile.data.fileStorage.daos.ActorDao
import com.sjmarsh.movies2mobile.data.fileStorage.daos.MovieDao
import java.io.File

class FileDataStorage(private val context: Context) : IDataStorage {

    private val movieFile = MovieFile(context)

    override fun movieDao(): IMovieDao {
        return MovieDao(movieFile)
    }

    override fun actorDao(): IActorDao {
        return ActorDao(movieFile)
    }

    override fun initializeLocalStorage(jsonString: String) {
        val targetFilePath = context.filesDir?.path + "/${Constants.MOVIE_DATA_FILE}"
        File(targetFilePath).writeText(jsonString)
    }
}