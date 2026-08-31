package it.unibo.progettomobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class], version = 2)
abstract class ProgettoMobileDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}