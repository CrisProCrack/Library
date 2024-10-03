import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

// Definir los elementos de navegación inferior como un data class
data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    // Lista de ítems de navegación
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Detalle del libro",
                icon = Icons.Filled.Book,
                route = Screens.BookDetail.route
            ),
            BottomNavigationItem(
                label = "Catálogo",
                icon = Icons.Filled.Bookmarks,
                route = Screens.Catalog.route
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

}