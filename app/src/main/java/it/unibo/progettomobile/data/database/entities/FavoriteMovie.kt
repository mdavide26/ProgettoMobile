package it.unibo.progettomobile.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteMovie(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val posterPath: String?,
    @ColumnInfo
    val overview: String,
    @ColumnInfo
    val voteAverage: Double,
    @ColumnInfo
    val genreId: Int? = null,
    @ColumnInfo
    val userEmail: String
)