package com.example.library.ui.screens.admin.users

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library.data.LibraryDatabase


class AdminUsersViewModelFactory(
    private val database: LibraryDatabase
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminUsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminUsersViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


