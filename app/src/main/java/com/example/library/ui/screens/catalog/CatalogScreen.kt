package com.example.library.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.library.R

@Preview
@Composable
fun FilterChipCarousel(){
    val labels = listOf("Label", "Label", "Label", "Label", "Label", "Label")
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(labels.size) { index ->
            FilterChip(
                selected = index == 1,
                onClick = {},
                label = { Text(labels[index]) }
            )
        }
    }
}

@Preview
@Composable
fun SortingControls(){
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.SwapVert, contentDescription = "Sort" )
        }
        Text(
            text = "Label",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.width(230.dp))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.List, contentDescription = "Lista")
        }
    }
}

@Preview
@Composable
fun Cards(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .width(120.dp)
                .height(168.dp),
            // shape = CutCornerShape(20.dp)
            elevation = CardDefaults.cardElevation(10.dp),
            //border = BorderStroke(3.dp,Color.Gray)
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    modifier = Modifier
                        .width(121.dp)
                        .height(120.dp),
                    painter = painterResource(id = R.drawable.wireframe_image),
                    contentDescription = "null",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Updated today ",
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

@Preview
@Composable
fun BookCardGrid() {
    val books = List(12) { "Title" } // Lista de libros ficticia
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(books.size) {
            Cards()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CatalogScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de volver */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de menú */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menú")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Filtro con Chips (Carrusel)
            FilterChipCarousel()

            Spacer(modifier = Modifier.height(16.dp))

            // Sorting Controls
            SortingControls()

            Spacer(modifier = Modifier.height(16.dp))

            // Card Grid
            BookCardGrid()
        }
    }
}
