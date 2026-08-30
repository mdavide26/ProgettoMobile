package it.unibo.progettomobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unibo.progettomobile.data.database.dao.MovieDAO
import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.User
import it.unibo.progettomobile.data.database.entities.Movie

@Database(
    entities=[
        User::class,
        Movie::class
    ] , version = 2
)
abstract class MovieDatabase : RoomDatabase () {
    abstract fun userDAO() : UserDAO
    abstract fun movieDAO() : MovieDAO
}