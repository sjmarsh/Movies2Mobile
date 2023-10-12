package com.sjmarsh.movies2mobile.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor")
class ActorEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo val firstName: String?,
    @ColumnInfo val lastName: String?,
    @ColumnInfo val sex: String?,
    @ColumnInfo val photo: String?,
    @ColumnInfo val fullName: String?,
    @ColumnInfo val dateOfBirth: String?
)