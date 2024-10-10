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

    var isAdmin = false // Propiedad para almacenar si el usuario es admin

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

            if (user != null) {
                _loginSuccess.value = true
                // Llama a checkIfAdmin para establecer el estado de administrador
                isAdmin = checkIfAdmin(user.email)
            } else {
                _loginSuccess.value = false
            }
        }
    }

    // Función para verificar si el usuario es administrador
    private suspend fun checkIfAdmin(email: String): Boolean {
        val user = database.userDao().getUserByEmail(email)
        return user?.isAdmin ?: false // Retorna false si no se encontró el usuario
    }
}
