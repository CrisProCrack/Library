package com.example.library.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val database: LibraryDatabase) : ViewModel() {

    private var _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private var _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private var _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private var _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private var _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    fun onNameChange(newName: String) { _name.value = newName }
    fun onEmailChange(newEmail: String) { _email.value = newEmail }
    fun onPhoneNumberChange(newPhoneNumber: String) { _phoneNumber.value = newPhoneNumber }
    fun onAddressChange(newAddress: String) { _address.value = newAddress }
    fun onPasswordChange(newPassword: String) { _password.value = newPassword }
    fun onConfirmPasswordChange(newConfirmPassword: String) { _confirmPassword.value = newConfirmPassword }

    fun register() {
        if (_password.value == _confirmPassword.value && _name.value.isNotEmpty() &&
            _email.value.isNotEmpty() && _phoneNumber.value.isNotEmpty() &&
            _address.value.isNotEmpty()) {

            val user = User(
                id = System.currentTimeMillis().toString(), // Generar un ID único
                name = _name.value,
                email = _email.value,
                phoneNumber = _phoneNumber.value,
                address = _address.value,
                password = _password.value // Recuerda que no es seguro almacenar contraseñas en texto plano
            )

            viewModelScope.launch {
                // Guardar el usuario en la base de datos
                database.userDao().insertUser(user)
                _registrationSuccess.value = true
            }
        } else {
            _registrationSuccess.value = false
        }
    }
}