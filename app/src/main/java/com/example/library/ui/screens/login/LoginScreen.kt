package com.example.library.ui.screens.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import com.example.library.data.LibraryDatabase
import com.example.library.ui.theme.LibraryTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (String, Boolean) -> Unit,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current)
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(database))
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LibraryTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Gestión de \n préstamos de \n libros",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Bienvenida
                Text(
                    text = "Bienvenido de vuelta",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen de usuario
                Image(
                    painter = painterResource(id = R.drawable.library_icon_png_5),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Correo") },
                    placeholder = { Text("Ingresa un correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
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
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de inicio de sesión
                Button(
                    onClick = {
                        viewModel.login()
                        showMessage = true // Muestra el mensaje después del intento de inicio de sesión
                        if (loginSuccess) {
                            // Guarda el userId en SharedPreferences
                            scope.launch {
                                saveUserIdToPreferences(context, email)
                            }
                            // Verifica si el usuario es administrador
                            val isAdminLogin = checkIfAdmin(email) // Implementa esta función según tu lógica
                            onLoginSuccess(email, isAdminLogin)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Iniciar sesión", color = Color.White)
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Texto de registro
                TextButton(onClick = { navController.navigate(Screens.Register.route) }) {
                    Text("¿No estás registrado? Regístrate", color = MaterialTheme.colorScheme.onSurface)
                }

                // Mensaje de éxito o error en el inicio de sesión
                if (showMessage) {
                    if (loginSuccess) {
                        Text("Inicio de sesión exitoso", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text("Credenciales incorrectas", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// Función para verificar si el usuario es administrador
private fun checkIfAdmin(email: String): Boolean {
    return email == "admin@example.com" // Cambia esto según tu lógica
}

// Función para guardar el userId en SharedPreferences
suspend fun saveUserIdToPreferences(context: Context, userId: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("user_id", userId).apply()
}

@Preview
@Composable
fun LoginScreenPreview() {
    LibraryTheme {
        LoginScreen(rememberNavController(), onLoginSuccess = { _, _ -> })
    }
}