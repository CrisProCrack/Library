package com.example.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.library.data.LibraryDatabase
import com.example.library.data.repository.UserRepository
import com.example.library.ui.navigation.BottomNavigationBar
import com.example.library.ui.navigation.AdminBottomNavigationBar
import com.example.library.ui.screens.login.LoginScreen
import com.example.library.ui.screens.register.RegisterScreen
import com.example.library.ui.theme.LibraryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: LibraryDatabase
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos y el repositorio
        database = LibraryDatabase.getDatabase(this)
        userRepository = UserRepository(database)

        // Inserta un usuario administrador si no existe
        CoroutineScope(Dispatchers.IO).launch {
            val existingAdmin = userRepository.getUserByEmail("admin@example.com")
            if (existingAdmin == null) {
                userRepository.insertAdminUser() // Método en el repositorio para insertar
            }
        }

        setContent {
            LibraryTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    // Estado para el inicio de sesión
                    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
                    var isAdmin by rememberSaveable { mutableStateOf(false) }

                    NavHost(navController = navController, startDestination = if (isLoggedIn) {
                        if (isAdmin) "admin_home" else "user_home"
                    } else {
                        "login"
                    }) {
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                onLoginSuccess = { email, isAdminLogin ->
                                    isLoggedIn = true
                                    isAdmin = isAdminLogin // true si es un admin
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }

                        composable("user_home") {
                            BottomNavigationBar(onLoginSuccess = { email, isAdmin ->
                                // Aquí puedes manejar lo que quieras después del inicio de sesión
                            }, isAdmin = isAdmin) // Pantalla principal del usuario
                        }

                        composable("admin_home") {
                            AdminBottomNavigationBar() // Pantalla principal del administrador
                        }
                    }
                }
            }
        }
    }
}