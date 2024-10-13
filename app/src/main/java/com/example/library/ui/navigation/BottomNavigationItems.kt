//BottomNavigationItems.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

// Definir los elementos de navegación inferior como un data class
data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Bookmarks,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Catálogo",
                icon = Icons.Filled.Bookmarks,
                route = Screens.Catalog.route
            ),
            BottomNavigationItem(
                label = "Buscar",
                icon = Icons.Filled.Search,
                route = Screens.Search.route
            ),
            BottomNavigationItem(
                label = "Prestado",
                icon = Icons.Filled.Book,
                route = Screens.RentedBooks.route
            )
        )
    }
}

// Screens actualizadas con la nueva estructura
sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Register : Screens("register")
    object BookDetail : Screens("book_detail")
    object Catalog : Screens("catalog")
    object Search : Screens("search")
    object RentedBooks : Screens("rented_books")
}
