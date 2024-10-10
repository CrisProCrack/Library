package com.example.library.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
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
import com.example.library.ui.screens.admin.AdminBooksScreen
import com.example.library.ui.screens.admin.AdminRentedBooksScreen
import com.example.library.ui.screens.admin.AdminUsersScreen

@Composable
fun AdminBottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                adminBottomNavigationItems().forEach { navigationItem ->
                    val isSelected = navigationItem.route == currentDestination?.route

                    NavigationBarItem(
                        selected = isSelected,
                        label = {
                            Text(
                                text = navigationItem.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
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

                // Botón para regresar al panel de usuario
                NavigationBarItem(
                    selected = false,
                    label = { Text("Usuario") },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
                    onClick = {
                        navController.navigate("user_panel") {
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AdminScreens.Users.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(AdminScreens.Users.route) {
                AdminUsersScreen(navController = navController)
            }
            composable(AdminScreens.Books.route) {
                AdminBooksScreen(navController = navController)
            }
            composable(AdminScreens.RentedBooks.route) {
                AdminRentedBooksScreen(navController = navController)
            }
        }
    }
}

fun adminBottomNavigationItems(): List<AdminNavigationItem> {
    return listOf(
        AdminNavigationItem(
            label = "Usuarios",
            icon = Icons.Filled.People,
            route = AdminScreens.Users.route
        ),
        AdminNavigationItem(
            label = "Libros",
            icon = Icons.Filled.Book,
            route = AdminScreens.Books.route
        ),
        AdminNavigationItem(
            label = "Libros Rentados",
            icon = Icons.AutoMirrored.Filled.LibraryBooks,
            route = AdminScreens.RentedBooks.route
        )
    )
}

data class AdminNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

sealed class AdminScreens(val route: String) {
    object Users : AdminScreens("admin_users")
    object Books : AdminScreens("admin_books")
    object RentedBooks : AdminScreens("admin_rented_books")
}
