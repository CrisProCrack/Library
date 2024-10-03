package com.example.library.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class LoginViewModel : ViewModel(){
    //Estados para el correo y la contrasena
    val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val _password = MutableStateFlow("")
    val password : StateFlow<String> = _password

    val _loginSuccess = MutableStateFlow(false)
    val loginSuccess : StateFlow<Boolean> = _loginSuccess

    //Actualizacion del correo
    fun onEmailChange(newEmail : String){
        _email.value = newEmail
    }

    //Actualizacion de la contrasena
    fun onPasswordChange(newPassword : String){
        _password.value = newPassword
    }

    //Funcion para manejar el inicio de sesion
    fun login(){
        viewModelScope.launch {
            //Aqui iria la logica de autenticacion
            if (_email.value == "admin" && _password.value == "admin"){
                _loginSuccess.value = true
            } else {
                _loginSuccess.value = false
            }
        }
    }
}