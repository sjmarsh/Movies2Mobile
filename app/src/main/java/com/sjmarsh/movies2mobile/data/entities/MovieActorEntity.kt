package com.sjmarsh.movies2mobile.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "movieActor", primaryKeys = ["movieId", "actorId"])
class MovieActorEntity (
    @ColumnInfo val movieId: Int,
    @ColumnInfo val actorId: Int
)