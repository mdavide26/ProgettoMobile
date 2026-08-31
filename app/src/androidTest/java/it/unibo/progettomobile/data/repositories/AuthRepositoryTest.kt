package it.unibo.progettomobile.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.unibo.progettomobile.data.database.ProgettoMobileDatabase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@RunWith(AndroidJUnit4::class)
class AuthRepositoryTest {

    private lateinit var db: ProgettoMobileDatabase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProgettoMobileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        authRepository = AuthRepository(db.userDAO())
    }

    @Test
    fun registrazioneELogin() = runTest {
        val registerResult = authRepository.register("mario@test.com", "password123", "Mario")
        assertTrue(registerResult.isSuccess, "La registrazione dovrebbe riuscire")
        println("REGISTER: $registerResult")

        val loginResult = authRepository.login("mario@test.com", "password123")
        assertTrue(loginResult.isSuccess, "Il login con credenziali corrette dovrebbe riuscire")
        println("LOGIN: $loginResult")
    }

    @Test
    fun failedLogin() = runTest {
        authRepository.register("mario@test.com", "password123", "Mario")

        val loginResult = authRepository.login("mario@test.com", "passwordSbagliata")
        assertFalse(loginResult.isSuccess, "Il login con password sbagliata dovrebbe fallire")
        println("LOGIN FALLITO COME ATTESO: ${loginResult.exceptionOrNull()?.message}")
    }

    @Test
    fun existingLogin() = runTest {
        authRepository.register("mario@test.com", "password123", "Mario")

        val secondRegister = authRepository.register("mario@test.com", "altraPassword", "Mario2")
        assertFalse(secondRegister.isSuccess, "La seconda registrazione con la stessa email dovrebbe fallire")
        println("SECONDA REGISTRAZIONE FALLITA COME ATTESO: ${secondRegister.exceptionOrNull()?.message}")
    }
}
