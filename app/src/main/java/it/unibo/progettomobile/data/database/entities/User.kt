package it.unibo.progettomobile.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (

    @PrimaryKey
    val email: String,

    @ColumnInfo
    val passwordHashed: String,

    @ColumnInfo
    var username: String,

    @ColumnInfo
    var profilePictureUri: String? = null

)
