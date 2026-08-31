package it.unibo.progettomobile.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.unibo.progettomobile.data.database.entities.User

@Dao
interface UserDAO {

    @Insert
    suspend fun addUser(user : User)

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String) : User?
}