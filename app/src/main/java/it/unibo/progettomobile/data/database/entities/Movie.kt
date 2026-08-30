package it.unibo.progettomobile.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    var tmdbId: Int,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var posterUrl: String?,
    @ColumnInfo
    var isWatched: Boolean = false,
    @ColumnInfo
    var userRating: Float? = null
)
