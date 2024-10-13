package com.example.library.ui.screens.admin.rentedbooks


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.library.data.LibraryDatabase
import androidx.lifecycle.ViewModelProvider



class RentedBooksViewModelFactory(private val database: LibraryDatabase) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RentedBooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RentedBooksViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
