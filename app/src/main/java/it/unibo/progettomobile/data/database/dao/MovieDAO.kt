package it.unibo.progettomobile.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.unibo.progettomobile.data.database.entities.FavoriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Query("SELECT * FROM FavoriteMovie WHERE userEmail = :userEmail")
    fun getAllFavorites(userEmail: String): Flow<List<FavoriteMovie>>

    @Upsert
    suspend fun insertFavorite(movie: FavoriteMovie)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovie)

    @Query("SELECT EXISTS(SELECT * FROM FavoriteMovie WHERE id = :id AND userEmail = :userEmail)")
    fun isFavorite(id: Int, userEmail: String): Flow<Boolean>
}