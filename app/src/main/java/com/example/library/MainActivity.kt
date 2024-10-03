package com.example.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.library.ui.navigation.BottomNavigationBar
import com.example.library.ui.theme.LibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryTheme {
                // Usamos un Surface para que el contenido herede el color de fondo del tema
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Llamamos al componente que tiene la navegación y barra inferior
                    BottomNavigationBar()
                }
            }
        }
    }
}