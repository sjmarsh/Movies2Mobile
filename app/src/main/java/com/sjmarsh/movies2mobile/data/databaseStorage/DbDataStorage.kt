package com.sjmarsh.movies2mobile.data.databaseStorage

import android.content.Context
import com.sjmarsh.movies2mobile.data.IActorDao
import com.sjmarsh.movies2mobile.data.IDataStorage
import com.sjmarsh.movies2mobile.data.IJsonToModel
import com.sjmarsh.movies2mobile.data.IMovieDao
import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieActorEntity
import com.sjmarsh.movies2mobile.data.entities.MovieEntity
import com.sjmarsh.movies2mobile.data.fileStorage.entities.MovieWithActorsEntity

class DbDataStorage(private val context: Context, private val jsonToModel: IJsonToModel) : IDataStorage {

    private val database : MovieDatabase = MovieDatabase(context)

    override fun movieDao(): IMovieDao {
        return database.movieDao()
    }

    override fun actorDao(): IActorDao {
        return database.actorDao()
    }

    override fun initializeLocalStorage(jsonString: String) {
        val importEntity = jsonToModel.convert(jsonString)
        if(importEntity !== null) {
            database.runInTransaction {
                run {
                    if (importEntity.movies !== null) {
                        val movieEntities = importEntity.movies!!.map { m ->
                            MovieEntity(
                                m.id,
                                m.title,
                                m.description,
                                m.releaseYear,
                                m.category,
                                m.classification,
                                m.format,
                                m.runningTime,
                                m.rating,
                                m.dateAdded,
                                m.coverArt
                            )
                        }
                        database.movieDao().initMovies(movieEntities)

                        val movieActors = getMovieActors(importEntity.movies)
                        if(movieActors !== null) {
                            database.movieActorDao().initMovieActors(movieActors)
                        }
                    }
                    if (importEntity.actors !== null) {
                        val actorEntities = importEntity.actors!!.map { a ->
                            ActorEntity(
                                a.id,
                                a.firstName,
                                a.lastName,
                                a.sex,
                                a.photo,
                                a.fullName,
                                a.dateOfBirth
                            )
                        }
                        database.actorDao().initActors(actorEntities)
                    }
                }
            }
        }
    }

    private fun getMovieActors(movies: List<MovieWithActorsEntity>?) : List<MovieActorEntity>? {
        if(movies == null) return null
        val movieActors = ArrayList<MovieActorEntity>()
        movies.forEach { m ->
            m.actors?.forEach { a -> movieActors.add(
                MovieActorEntity(m.id, a.id)
            ) }
        }
        return movieActors.toList()
    }

}