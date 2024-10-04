package com.example.library.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val database: LibraryDatabase) : ViewModel() {
    val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            // Verifica si las credenciales son correctas
            val user = database.userDao().getUserByEmailAndPassword(_email.value, _password.value)
            _loginSuccess.value = user != null // true si el usuario fue encontrado
        }
    }
}