package it.unibo.progettomobile.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var date: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    val imageUri: String?
)

@Entity
data class FavoriteMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String,
    val voteAverage: Double,
    val genreId: Int? = null   // <- AGGIUNTO
)
