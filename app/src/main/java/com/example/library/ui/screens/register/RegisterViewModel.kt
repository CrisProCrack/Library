package com.example.library.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class RegisterViewModel : ViewModel() {
    var _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    // Nuevas propiedades para la contraseña y confirmación
    var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    var _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    //Funciones para actualizar los estados
    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun onAddressChange(newAddress: String) {
        _address.value = newAddress
    }

    // Funciones para manejar las contraseñas
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    // Función para manejar el registro
    fun register() {
        // Validar campos
        if (_name.value.isNotEmpty() &&
            _email.value.isNotEmpty() &&
            _phoneNumber.value.isNotEmpty() &&
            _address.value.isNotEmpty() &&
            _password.value.isNotEmpty() &&
            _confirmPassword.value.isNotEmpty() &&
            _password.value == _confirmPassword.value) {
            // Aquí va la lógica de registro (por ejemplo, llamar a una API)
            _registrationSuccess.value = true
        } else {
            _registrationSuccess.value = false
        }
    }
}