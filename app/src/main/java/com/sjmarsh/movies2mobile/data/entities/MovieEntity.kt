package com.sjmarsh.movies2mobile.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "movie")
class MovieEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String?,
    @ColumnInfo val releaseYear: String?,
    @ColumnInfo val category: String?,
    @ColumnInfo val classification: String?,
    @ColumnInfo val format: String?,
    @ColumnInfo val runningTime: String?,
    @ColumnInfo val rating: Int?,
    @ColumnInfo val dateAdded: String?,
    @ColumnInfo val coverArt: String?
)