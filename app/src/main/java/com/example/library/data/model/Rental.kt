package com.example.library.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class Rental(
    @PrimaryKey val id: String, // ID único para cada renta
    val userId: String,         // ID del usuario
    val bookId: String,         // ID del libro
    val rentalDate: String,
    var status: RentalStatus     // Estado de la renta
)

enum class RentalStatus {
    RETURNED,
    LATE,
    NOT_RETURNED
}
