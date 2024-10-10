package com.example.library.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.library.ui.screens.bookdetail.BookDetailScreen
import com.example.library.ui.screens.catalog.CatalogScreen
import com.example.library.ui.screens.login.LoginScreen
import com.example.library.ui.screens.register.RegisterScreen
import com.example.library.ui.screens.search.SearchScreen

@Composable
fun BottomNavigationBar(onLoginSuccess: (String, Boolean) -> Unit, isAdmin: Boolean) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentDestination?.route !in listOf(Screens.Login.route, Screens.Register.route)) {
                NavigationBar {
                    // Definir los elementos de navegación
                    val navigationItems = listOf(
                        NavigationItem("Catalog", "catalog", Icons.Filled.Bookmarks),
                        NavigationItem("Search", "search", Icons.Filled.Search)
                    )

                    navigationItems.forEach { navigationItem ->
                        val isSelected = navigationItem.route == currentDestination?.route

                        NavigationBarItem(
                            selected = isSelected,
                            label = { Text(navigationItem.label, style = MaterialTheme.typography.labelMedium) },
                            icon = { Icon(imageVector = navigationItem.icon, contentDescription = navigationItem.label) },
                            onClick = {
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    // Mostrar el botón de administración solo si es un administrador
                    if (isAdmin) {
                        NavigationBarItem(
                            selected = false,
                            label = { Text("Admin") },
                            icon = { Icon(Icons.Filled.Settings, contentDescription = "Admin") },
                            onClick = {
                                navController.navigate("admin_panel") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Catalog.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screens.Login.route) {
                LoginScreen(
                    navController = navController,
                    onLoginSuccess = onLoginSuccess
                )
            }
            composable(Screens.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(Screens.Catalog.route) {
                CatalogScreen(navController = navController)
            }
            composable(Screens.BookDetail.route) {
                BookDetailScreen(navController = navController)
            }
            composable(Screens.Search.route) {
                SearchScreen(navController = navController)
            }
            // Composable para el panel de administración
            composable("admin_panel") {
                AdminBottomNavigationBar() // Asegúrate de que esto también reciba el navController
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
