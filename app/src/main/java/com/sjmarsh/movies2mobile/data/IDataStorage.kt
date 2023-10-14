package com.sjmarsh.movies2mobile.data

interface IDataStorage {
    fun movieDao () : IMovieDao
    fun actorDao () : IActorDao
}