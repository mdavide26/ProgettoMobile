package it.unibo.progettomobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.FavoriteMovie
import it.unibo.progettomobile.data.database.entities.User

@Database(
    entities = [FavoriteMovie::class, User::class],
    version = 6
)
abstract class ProgettoMobileDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun movieDAO() : MovieDAO
}