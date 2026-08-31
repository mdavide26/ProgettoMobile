package it.unibo.progettomobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.User

@Database(
    entities = [FavoriteMovie::class, User::class],
    version = 5
)
abstract class ProgettoMobileDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
    abstract fun userDAO(): UserDAO
}