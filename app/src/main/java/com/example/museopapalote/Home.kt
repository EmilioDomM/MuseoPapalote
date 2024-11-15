package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.landingpage.components.NavBar.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 0.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ObrasDeInteresSection()
            Spacer(modifier = Modifier.height(16.dp))
            MapaInteractivoSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    Box {
        SmallTopAppBar(
            title = {
                Text(
                    "Hola {Usuario}",
                    color = Color.White,
                    fontSize = 40.sp
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(0xFF4CAF50) // Fondo verde
            ),
            actions = {
                IconButton(onClick = { /* Acción al hacer clic */ },
                    modifier = Modifier
                        .offset(x =(-30).dp)
                        .size(50.dp)
                    ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier
                            .size(720.dp)
                            .background(Color.White, CircleShape)
                    )
                }
            }
        )
    }
}



@Composable
fun ObrasDeInteresSection() {
    val imageList = listOf(
        R.drawable.obra_1,
        R.drawable.obra_2,
        R.drawable.obra_3,
        R.drawable.obra_4,
        R.drawable.seccion_1,
        R.drawable.seccion_2,
        R.drawable.seccion_3,
        R.drawable.seccion_4
    )

    val imageDescriptions = listOf(
        "Descripción detallada de la obra 1",
        "Descripción detallada de la obra 2",
        "Descripción detallada de la obra 3",
        "Descripción detallada de la obra 4",
        "Descripción de la sección 1",
        "Descripción de la sección 2",
        "Descripción de la sección 3",
        "Descripción de la sección 4"
    )

    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Obras de Interés",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(imageList.chunked(2)) { imageChunk ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    imageChunk.forEachIndexed { _, imageRes ->
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .border(1.dp, MaterialTheme.colorScheme.onSurface)
                                .clickable { selectedImageIndex = imageList.indexOf(imageRes) }
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = imageDescriptions[imageList.indexOf(imageRes)],
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

    selectedImageIndex?.let { index ->
        Dialog(onDismissRequest = { selectedImageIndex = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Botón de cerrar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { selectedImageIndex = null }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Imagen detallada
                    Image(
                        painter = painterResource(id = imageList[index]),
                        contentDescription = imageDescriptions[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = imageDescriptions[index],
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp),
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón "Ver en mapa"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { /* Lógica para abrir un mapa */ }) {
                            Text("Ver en mapa")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun MapaInteractivoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 12.dp)
    ) {
        Text(
            text = "Mapa Interactivo",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa Interactivo",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .padding(4.dp) // Menos padding para hacer el borde visualmente más pequeño
        )
    }
}



