package com.example.library.data.repository

import com.example.library.data.LibraryDatabase
import com.example.library.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: LibraryDatabase) {

    suspend fun insertAdminUser() {
        val adminUser = User(
            id = "admin1",
            name = "Admin User",
            email = "admin@example.com",
            phoneNumber = "1234567890",
            address = "123 Admin St.",
            password = "admin_password", // Asegúrate de encriptar la contraseña
            isAdmin = true
        )

        withContext(Dispatchers.IO) {
            database.userDao().insertUser(adminUser)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return withContext(Dispatchers.IO) {
            database.userDao().getUserByEmail(email)
        }
    }

    // Otros métodos para acceder a datos de usuarios
}
