package com.example.library.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: ImageVector) {
    object BookDetail : Screen("book_detail", Icons.Filled.Book)
    object Catalog : Screen("catalog", Icons.Filled.Bookmarks)
    object Search : Screen("search", Icons.Filled.Search)

}
