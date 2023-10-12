package com.sjmarsh.movies2mobile.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sjmarsh.movies2mobile.data.entities.ActorEntity

@Dao
interface ActorDao {

    @Query("SELECT * FROM actor")
    fun getAll() : List<ActorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg actor: ActorEntity)

    @Insert
    fun insertAll(actors: List<ActorEntity>)

    @Delete
    fun delete(actor: ActorEntity)

    @Query("DELETE FROM actor")
    fun deleteAll()

    @Transaction
    open fun initActors(actors: List<ActorEntity>){
        deleteAll()
        insertAll(actors)
    }
}