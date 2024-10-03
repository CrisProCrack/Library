package com.example.library.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.library.R
import com.example.library.ui.screens.login.LoginViewModel
import com.example.library.ui.theme.LibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavController
) {
    // Observamos los estados desde el ViewModel
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

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
                fontSize = 32.sp,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Bienvenida
            Text(
                text = "Bienvenido de vuelta",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de usuario
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                placeholder = { Text("Ingresa una contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    viewModel.login()
                    if (loginSuccess) {
                        navController.navigate(Screens.Catalog.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text("Iniciar sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Texto de registro
            TextButton(onClick = { navController.navigate(Screens.Register.route) }) {
                Text("¿No estás registrado? Regístrate", color = Color.Black)
            }

            // Mensaje de éxito en el inicio de sesión
            if (loginSuccess) {
                Text("Inicio de sesión exitoso", color = MaterialTheme.colorScheme.primary)
            } else {
                Text("Credenciales incorrectas", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

//class PreviewLoginViewModel : LoginViewModel() {
//    init {
//        // Simulando el valor de email y password
//        _email.value = "usuario@ejemplo.com"
//        _password.value = "contraseña123"
//        _loginSuccess.value = true
//    }
//}
//
//@Composable
//@Preview
//fun LoginScreenPreview() {
//    val previewViewModel = PreviewLoginViewModel()
//
//    LibraryTheme {
//        //LoginScreen(viewModel = previewViewModel)
//    }
//}
