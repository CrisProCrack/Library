package com.example.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.library.data.model.Rental
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: Rental)

    @Query("SELECT * FROM rentals")
    fun getAllRentals(): Flow<List<Rental>>

    @Query("SELECT * FROM rentals WHERE userId = :userId")
    fun getRentalsByUserId(userId: String): Flow<List<Rental>>
}