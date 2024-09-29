package com.example.library.ui.screens.register

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.library.ui.theme.LibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    Scaffold (
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
                value = "",
                onValueChange = {},
                label = { Text("Nombre") },
                placeholder = { Text("Ingresa un nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Campo de correo
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Correo") },
                placeholder = { Text("Ingresa un correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Campo de número de telofono
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Número de teléfono") },
                placeholder = { Text("Ingresa un número telefónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier =Modifier.height(56.dp))

            // Campo de número de direccion
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Dirección") },
                placeholder = { Text("Ingresa una dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier =Modifier.height(51.dp))

            Button(
                onClick = { /* Acción de iniciar sesión */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(modifier =Modifier.height(22.dp))

            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "¿Ya tienes una cuenta? Iniciar sesión", color = Color.Black)
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    LibraryTheme {
        RegisterScreen()
    }
}