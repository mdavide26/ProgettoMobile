package it.unibo.progettomobile.data.repositories

import android.content.ContentResolver
import android.net.Uri
import it.unibo.progettomobile.data.database.Trip
import it.unibo.progettomobile.data.database.TripsDAO
import it.unibo.progettomobile.utils.saveImageToStorage
import kotlinx.coroutines.flow.Flow

class TripsRepository(
    private val dao: TripsDAO,
    private val contentResolver: ContentResolver
) {
    val trips: Flow<List<Trip>> = dao.getAll()

    suspend fun upsert(trip: Trip) {
        if (trip.imageUri != null) {
            val imageUri = saveImageToStorage(
                Uri.parse(trip.imageUri),
                contentResolver,
                "TravelDiary_Trip${trip.name}"
            )
            dao.upsert(trip.copy(imageUri = imageUri.toString()))
        } else {
            dao.upsert(trip)
        }
    }

    suspend fun delete(trip: Trip) = dao.delete(trip)
}
