package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ObrasDeInteresSection()
            Spacer(modifier = Modifier.height(30.dp))
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
    val obras = listOf(
        Obra(
            id = 1,
            title = "Título de la obra 1",
            thumbnailImageRes = R.drawable.obra_3,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_1,
                    title = "Imagen 1",
                    description = "Primera imagen de la obra 1."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_1,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 1."
                )
            )
        ),
        Obra(
            id = 2,
            title = "Título de la obra 2",
            thumbnailImageRes = R.drawable.obra_4,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_2,
                    title = "Imagen 1",
                    description = "Primera imagen de la obra 2."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_2,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 2."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_3,
                    title = "Imagen 3",
                    description = "Tercera imagen de la obra 2."
                )
            )
        ),
        Obra(
            id = 3,
            title = "Título de la obra 3",
            thumbnailImageRes = R.drawable.obra_3,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_1,
                    title = "Imagen 1",
                    description = "Primera imagen de la obra 3."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_1,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 3."
                )
            )
        ),
        Obra(
            id = 4,
            title = "Título de la obra 4",
            thumbnailImageRes = R.drawable.obra_4,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_2,
                    title = "Imagen 1",
                    description = "Primera imagen de la obra 4."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_2,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 4."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_3,
                    title = "Imagen 3",
                    description = "Tercera imagen de la obra 4."
                )
            )
        ),
        Obra(
            id = 5,
            title = "Título de la obra 5",
            thumbnailImageRes = R.drawable.obra_3,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_1,
                    title = "Imagen 1",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n"
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_1,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 1."
                )
            )
        ),
        Obra(
            id = 6,
            title = "Título de la obra 6",
            thumbnailImageRes = R.drawable.obra_4,
            imagesWithDescriptions = listOf(
                ImageWithDetails(
                    imageRes = R.drawable.obra_2,
                    title = "Imagen 1",
                    description = "Primera imagen de la obra 2."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_2,
                    title = "Imagen 2",
                    description = "Segunda imagen de la obra 2."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_3,
                    title = "Imagen 3",
                    description = "Tercera imagen de la obra 2."
                ),
                ImageWithDetails(
                    imageRes = R.drawable.seccion_3,
                    title = "Imagen 4",
                    description = "Cuarta imagen de la obra 2"
                )
            )
        ),
    )

    var selectedObraId by remember { mutableStateOf<Int?>(null) }
    var selectedImageWithDetails by remember { mutableStateOf<ImageWithDetails?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título de la sección
        Text(
            text = "Secciones del museo",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Contenido con scroll interno limitado
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp) // Limitar la altura para evitar conflictos de scroll
        ) {
            items(obras.chunked(2)) { rowObras ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()) // Scroll horizontal para filas
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowObras.forEach { obra ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f) // Mantener proporción cuadrada
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.onSurface)
                                .clickable { selectedObraId = obra.id }
                        ) {
                            Image(
                                painter = painterResource(id = obra.thumbnailImageRes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    if (rowObras.size < 2) {
                        Spacer(modifier = Modifier.weight(1f)) // Espaciador para alinear
                    }
                }
            }
        }
    }

    // Diálogo para obra seleccionada
    selectedObraId?.let { id ->
        val obra = obras.find { it.id == id }
        Dialog(
            onDismissRequest = { selectedObraId = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Botón de regresar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = { selectedObraId = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Mostrar obra seleccionada
                    obra?.let {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Imágenes de la obra
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            it.imagesWithDescriptions.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    pair.forEach { imageWithDetails ->
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedImageWithDetails = imageWithDetails },
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surface,
                                                        shape = MaterialTheme.shapes.medium
                                                    )
                                                    .shadow(4.dp, MaterialTheme.shapes.medium)
                                                    .padding(12.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = imageWithDetails.imageRes),
                                                    contentDescription = imageWithDetails.description,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(MaterialTheme.shapes.medium)
                                                )
                                            }

                                            // Título de la imagen
                                            Text(
                                                text = imageWithDetails.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    selectedImageWithDetails?.let { image ->
        Dialog(onDismissRequest = { selectedImageWithDetails = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Contenido principal (imagen y texto)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp), // Espacio para el botón
                    horizontalAlignment = Alignment.CenterHorizontally // Centra todo horizontalmente
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { selectedImageWithDetails = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // La imagen ocupa el 90% del ancho
                            .aspectRatio(1f) // Proporción cuadrada
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                            .shadow(8.dp, MaterialTheme.shapes.medium) // Sombra sutil
                            .padding(16.dp) // Espaciado interno
                    ) {
                        Image(
                            painter = painterResource(id = image.imageRes),
                            contentDescription = image.description,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Texto de la descripción
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally // Centra el texto
                    ) {
                        Text(
                            text = "Descripción:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = image.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center // Centra el texto dentro de su línea
                        )
                    }
                }

                // Botón en la parte inferior izquierda
                Button(
                    onClick = { /* Implementar lógica de revisar en mapa interactivo */ },
                    modifier = Modifier
                        .align(Alignment.BottomStart) // Posiciona el botón en la esquina inferior izquierda
                        .padding(16.dp) // Margen adicional para separarlo del borde
                ) {
                    Text(text = "Revisar en mapa interactivo")
                }
            }
        }
    }
}



// Modelos de datos
data class Obra(
    val id: Int,
    val title: String,
    val thumbnailImageRes: Int,
    val imagesWithDescriptions: List<ImageWithDetails>
)

data class ImageWithDetails(
    val imageRes: Int,
    val title: String,
    val description: String
)



@Composable
fun MapaInteractivoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp) // Espaciado general más consistente
    ) {
        // Título de la sección
        Text(
            text = "Mapa Interactivo",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp) // Separación del título respecto a la imagen
        )

        // Caja que contiene la imagen con un diseño estilizado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f) // Relación de aspecto para mantener proporciones
                .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                .background(MaterialTheme.colorScheme.surface) // Fondo que resalta
                .shadow(8.dp, RoundedCornerShape(16.dp)) // Sombra suave
        ) {
            Image(
                painter = painterResource(id = R.drawable.mapa),
                contentDescription = "Mapa Interactivo",
                modifier = Modifier
                    .fillMaxSize() // Asegura que la imagen ocupe todo el espacio del contenedor
                    .clip(RoundedCornerShape(16.dp)) // Asegura que la imagen respete los bordes redondeados
            )
        }
    }
}




