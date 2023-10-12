package com.sjmarsh.movies2mobile.data

import android.content.Context
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.models.MovieModel
import java.text.SimpleDateFormat

class DataStoreSqlBased : IDataStore {

    val database : MovieDatabase
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    constructor(context: Context){
        database = MovieDatabase(context)
    }
    override suspend fun movies(): List<MovieModel> {
        var movieModels = listOf<MovieModel>()
        database.runInTransaction(
            Runnable {
                run {
                    val movieEntities = database.movieDao().getAll()
                    if(movieEntities !== null && movieEntities.any()) {
                        movieModels = movieEntities.map{m -> MovieModel(m.id, m.title, m.description, m.releaseYear, m.title, m.classification, m.format, m.runningTime, m.rating, null, m.coverArt, null)}
                    }
                }
            }
        )
        return movieModels;
    }

    override suspend fun actors(): List<ActorModel> {
        var actorModels = listOf<ActorModel>()
        database.runInTransaction(
            Runnable {
                run {
                    val actorEntities = database.actorDao().getAll()
                    if(actorEntities !== null && actorEntities.any()) {
                        actorModels = actorEntities.map{a -> ActorModel(a.id, a.firstName, a.lastName, a.sex, a.photo, a.fullName, null) }
                    }
                }
            }
        )

        return actorModels
    }
}