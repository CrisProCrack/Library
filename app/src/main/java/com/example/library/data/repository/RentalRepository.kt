package com.example.library.data.repository

import com.example.library.data.dao.RentalDao
import com.example.library.data.model.Rental
import kotlinx.coroutines.flow.Flow

class RentalRepository(private val rentalDao: RentalDao) {
    fun getAllRentals(): Flow<List<Rental>> = rentalDao.getAllRentals()

    suspend fun updateRentalStatus(rental: Rental) {
        rentalDao.insertRental(rental) // Esto también actualizará el estado si ya existe
    }
}