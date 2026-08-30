package it.unibo.progettomobile.data.repositories

import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.User
import it.unibo.progettomobile.utils.PasswordHasher

class AuthRepository (
    private val userDAO: UserDAO
){

    suspend fun register(email : String, password : String, username : String) : Result<Unit> {

        return try {
            val existing = userDAO.getUserByEmail(email)
            if(existing != null) {
                return Result.failure(Exception("Utente giá registrato"))
            }
            val hash = PasswordHasher.hash(password)
            userDAO.addUser(User(email = email, passwordHashed = hash, username = username))
            Result.success(Unit)
        } catch (e : Exception) {
            Result.failure(e)
        }

    }

    suspend fun login(email : String, password: String) : Result<Unit> {
        val existing = userDAO.getUserByEmail(email)
            ?: return Result.failure(Exception("Email non valida"))

        return if (PasswordHasher.verify(password, existing.passwordHashed)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Password non valida"))
        }
    }

}