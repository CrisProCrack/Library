package com.example.library.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String, // ID único para cada usuario
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val password: String // Puedes almacenar la contraseña, pero es recomendable no hacerlo en texto plano
)
