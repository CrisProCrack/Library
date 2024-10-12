//Screen.kt
package com.example.library.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Catalog : Screen("catalog", Icons.Filled.Bookmarks)
    object Search : Screen("search", Icons.Filled.Search)

    // Ruta dinámica para BookDetail que acepta un parámetro `bookId`
    object BookDetail : Screen("book_detail/{bookId}", Icons.Filled.Book) {
        // Función para construir la ruta con el ID del libro
        fun createRoute(bookId: String) = "book_detail/$bookId"
    }
}
