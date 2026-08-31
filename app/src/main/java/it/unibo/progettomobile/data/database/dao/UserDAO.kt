package it.unibo.progettomobile.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.progettomobile.data.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user : User)

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String) : User?

    @Query("SELECT * FROM User WHERE email = :email")
    fun observeUserByEmail(email: String): Flow<User?>

    @Update
    suspend fun updateUser(user : User)
}