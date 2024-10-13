package com.example.library.ui.screens.bookdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library.data.LibraryDatabase

class BookDetailViewModelFactory(
    private val database: LibraryDatabase
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}