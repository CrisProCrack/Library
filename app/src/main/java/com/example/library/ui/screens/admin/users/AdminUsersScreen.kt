package com.example.library.ui.screens.admin.users

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.library.data.model.User

@Composable
fun AdminUsersScreen(navController: NavController, adminViewModel: AdminUsersViewModel = viewModel()) {
    val users by adminViewModel.users.collectAsState()

    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Rented Books by Users",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users.size) { index ->
                UserCard(
                    user = users[index],
                    onDeleteClick = {
                        selectedUser = users[index]
                        showConfirmationDialog = true
                    }
                )
            }
        }
    }

    if (showConfirmationDialog && selectedUser != null) {
        ConfirmDeleteDialog(
            user = selectedUser!!,
            onConfirm = {
                adminViewModel.deleteUser(selectedUser!!)
                showConfirmationDialog = false
                selectedUser = null
            },
            onDismiss = {
                showConfirmationDialog = false
                selectedUser = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: User, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User: ${user.name}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Email: ${user.email}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text(text = "Delete User")
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(user: User, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete User") },
        text = { Text(text = "Are you sure you want to delete ${user.name}? This action cannot be undone.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Yes, Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}