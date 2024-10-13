package com.example.library.ui.screens.admin.books

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library.data.LibraryDatabase

class AdminBooksViewModelFactory(
    private val database: LibraryDatabase
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminBooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminBooksViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}