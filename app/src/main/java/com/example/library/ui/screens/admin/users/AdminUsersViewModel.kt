package com.example.library.ui.screens.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminUsersViewModel(private val database: LibraryDatabase) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _users.value = database.userDao().getAllUsers()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _users.value -= user
            loadUsers() // Refresca la lista tras eliminar
        }
    }
}