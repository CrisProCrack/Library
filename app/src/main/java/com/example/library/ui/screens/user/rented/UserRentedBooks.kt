package com.example.library.ui.screens.user.rented

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRentedBooks(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current),
    rentedBooksViewModel: UserRentedBooksViewModel = viewModel(factory = UserRentedBooksViewModelFactory(database))
) {

    val rentedBooks by rentedBooksViewModel.rentedBooks.collectAsState()
    val books by rentedBooksViewModel.books.collectAsState()
    val scope = rememberCoroutineScope()

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val filteredBooks = remember(selectedDate.value, rentedBooks) {
        rentedBooks.filter {
            try {
                val rentalDate = LocalDate.parse(it.rentalDate, dateFormatter)
                rentalDate.isEqual(selectedDate.value)
            } catch (e: Exception) {
                false
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Libros Rentados") }
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
            // Date Picker Button
            Text(
                text = "Fecha del préstamo: ${selectedDate.value.format(displayFormatter)}",
                textAlign = TextAlign.Center
            )

            Button(onClick = { showDatePicker = true }) {
                Text("Seleccionar fecha")
            }

            // Show Date Picker Dialog
            if (showDatePicker) {
                DatePickerModalInput(
                    initialDate = selectedDate.value,
                    onDateSelected = { date ->
                        selectedDate.value = date
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            // Rented Books List
            if (filteredBooks.isEmpty()) {
                Text("No hay libros rentados en la fecha seleccionada.")
            } else {
                LazyColumn {
                    items(filteredBooks) { rental ->
                        val book = books.find { it.id == rental.bookId }
                        book?.let {
                            RentedBookCard(rental, it)
                            Spacer(Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = it
                    }
                    val selectedDate = LocalDate.of(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).plusDays(1)
                    onDateSelected(selectedDate)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun RentedBookCard(rental: Rental, book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = book.imageResId),
                contentDescription = book.title,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Estado: ${rental.status}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Fecha del préstamo: ${rental.rentalDate}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun UserRentedBooksPreview() {
    UserRentedBooks(rememberNavController())
}