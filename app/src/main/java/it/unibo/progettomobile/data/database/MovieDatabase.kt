package it.unibo.progettomobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.User

@Database(
    entities=[
        User::class
    ] , version = 1
)
abstract class MovieDatabase : RoomDatabase () {
    abstract fun userDAO() : UserDAO
}