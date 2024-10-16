package com.example.library.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.library.data.LibraryDatabase
import com.example.library.ui.theme.LibraryTheme
import com.example.library.ui.screens.register.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current)
) {
    val viewModel: RegisterViewModel = viewModel(factory = ViewModelFactory(database))
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val address by viewModel.address.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()
    val passwordsMatch = password == confirmPassword

    var showMessage by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del mensaje
    var passwordVisible by remember { mutableStateOf(false) } // Estado para ver/ocultar contraseña
    var confirmPasswordVisible by remember { mutableStateOf(false) } // Estado para ver/ocultar confirmación de contraseña

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            navController.navigate(Screens.Login.route) {
                popUpTo(Screens.Register.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Registro") },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de nombre
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre") },
                placeholder = { Text("Ingresa un nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de correo
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo") },
                placeholder = { Text("Ingresa un correo") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de número de teléfono
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChange(it) },
                label = { Text("Número de teléfono") },
                placeholder = { Text("Ingresa un número telefónico") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de dirección
            OutlinedTextField(
                value = address,
                onValueChange = { viewModel.onAddressChange(it) },
                label = { Text("Dirección") },
                placeholder = { Text("Ingresa una dirección") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                placeholder = { Text("Ingresa una contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de confirmación de contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirma Contraseña") },
                placeholder = { Text("Confirma tu contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (passwordsMatch) {
                        viewModel.register()
                        showMessage = true // Muestra el mensaje después de intentar registrar
                    } else {
                        showMessage = true // Muestra el mensaje si las contraseñas no coinciden
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = passwordsMatch // Desactiva el botón si las contraseñas no coinciden
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Mensaje de éxito o error
            if (showMessage) {
                when {
                    registrationSuccess -> Text("Registro exitoso", color = MaterialTheme.colorScheme.primary)
                    !passwordsMatch -> Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
                    else -> Text("Por favor, completa todos los campos", color = MaterialTheme.colorScheme.error)
                }
            }

            // Texto de iniciar sesión
            TextButton(onClick = { navController.navigate(Screens.Login.route) }) {
                Text(text = "¿Ya tienes una cuenta? Iniciar sesión", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
