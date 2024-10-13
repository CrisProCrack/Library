package com.example.library.ui.screens.user.rented

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library.data.LibraryDatabase


class UserRentedBooksViewModelFactory(private val database: LibraryDatabase) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRentedBooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserRentedBooksViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}