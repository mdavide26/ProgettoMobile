package it.unibo.progettomobile.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.progettomobile.data.database.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM Movie WHERE isWatched = 0")
    fun getWatchlist(): Flow<List<Movie>>

    @Query("SELECT * FROM Movie WHERE isWatched = 1")
    fun getWatched(): Flow<List<Movie>>

    @Delete
    suspend fun delete(movie: Movie)
}