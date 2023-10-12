package com.sjmarsh.movies2mobile.data

import android.content.Context
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel
import com.sjmarsh.movies2mobile.ui.extensions.toDate

class DataStoreSqlBased(context: Context) : IDataStore {

    private val database : MovieDatabase

    init {
        database = MovieDatabase(context)
    }
    override suspend fun movies(): List<MovieModel> {
        var movieModels = listOf<MovieModel>()
        database.runInTransaction {
            run {
                val movieEntities = database.movieDao().getAll()
                if (movieEntities.any()) {
                    movieModels = movieEntities.map { m ->
                        MovieModel(
                            m.id,
                            m.title,
                            m.description,
                            m.releaseYear,
                            m.title,
                            m.classification,
                            m.format,
                            m.runningTime,
                            m.rating,
                            m.dateAdded.toDate(),
                            m.coverArt,
                            null
                        )
                    }
                }
            }
        }
        return movieModels;
    }

    override suspend fun actors(): List<ActorModel> {
        var actorModels = listOf<ActorModel>()
        database.runInTransaction {
            run {
                val actorEntities = database.actorDao().getAll()
                if (actorEntities.any()) {
                    actorModels = actorEntities.map { a ->
                        ActorModel(
                            a.id,
                            a.firstName,
                            a.lastName,
                            a.sex,
                            a.photo,
                            a.fullName,
                            a.dateOfBirth.toDate()
                        )
                    }
                }
            }
        }

        return actorModels
    }
}