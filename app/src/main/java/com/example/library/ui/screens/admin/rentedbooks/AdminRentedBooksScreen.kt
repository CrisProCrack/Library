package com.example.library.ui.screens.admin.rentedbooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import java.util.*

data class RentedBook(
    val title: String,
    val imageResId: Int,
    var status: String,
    val returnDate: Date,
    val daysOverdue: Int,
    val daysLeft: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRentedBooksScreen(navController: NavController) {
    val rentedBooks = remember {
        mutableStateListOf(
            RentedBook("El Alquimista", R.drawable.wireframe_image, "Entregado", Date(), 0, 7),
            RentedBook("1984", R.drawable.wireframe_image, "Por entregar", Date(), 0, 3),
            RentedBook("Cien años de soledad", R.drawable.wireframe_image, "Retrasado", Date(), 5, 0)
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestión de Libros Rentados") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn {
                items(rentedBooks) { book ->
                    RentedBookAdminCard(book) { newStatus ->
                        book.status = newStatus
                    }
                }
            }
        }
    }
}

@Composable
fun RentedBookAdminCard(book: RentedBook, onStatusChange: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = book.imageResId),
                    contentDescription = book.title,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Estado: ${book.status}", style = MaterialTheme.typography.bodySmall)
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Fecha de entrega: ${book.returnDate}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp)) // Espacio adicional
                Text(text = "Días retrasados: ${book.daysOverdue}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp)) // Espacio adicional
                Text(text = "Días restantes: ${book.daysLeft}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp)) // Espacio adicional

                // Status Change Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconWithLabel(
                        icon = Icons.Default.Check,
                        label = "Entregado",
                        onClick = { onStatusChange("Entregado") }
                    )
                    IconWithLabel(
                        icon = Icons.Default.Schedule,
                        label = "Por Entregar",
                        onClick = { onStatusChange("Por entregar") }
                    )
                    IconWithLabel(
                        icon = Icons.Default.Warning,
                        label = "Retrasado",
                        onClick = { onStatusChange("Retrasado") }
                    )
                }
            }
        }
    }
}

@Composable
fun IconWithLabel(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick)
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Normal)
    }
}

@Preview
@Composable
fun AdminRentedBooksPreview() {
    AdminRentedBooksScreen(rememberNavController())
}
