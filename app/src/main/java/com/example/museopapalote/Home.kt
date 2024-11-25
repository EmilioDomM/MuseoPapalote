package com.example.museopapalote

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.landingpage.components.NavBar.BottomNavigationBar
import com.example.museopapalote.ui.theme.veige
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star



@Composable
fun Home(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC))
    ) {
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
                    .background(Color(0xFFF5F5DC))

            ) {
                Spacer(modifier = Modifier.height(20.dp))

                NoticiasSection()

                Spacer(modifier = Modifier.height(20.dp))

                ObrasDeInteresSection()
                Spacer(modifier = Modifier.height(30.dp))
                MapaInteractivoSection()
            }
        }
    }
}

@Composable
fun TopBar() {
    var username by remember { mutableStateOf("Usuario") }

    // Obtener el userID del usuario autenticado
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        username = document.getString("username") ?: "Usuario"
                    }
                }
                .addOnFailureListener {
                    username = "Error"
                }
        } else {
            username = "Invitado"
        }
    }

    // Diseño de la barra superior
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF64A70B)) // Fondo verde
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hola $username",
                color = Color.White,
                fontSize = 40.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { /* Acción al hacer clic */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color(0xFF64A70B),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}



@Composable
fun NoticiasSection() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp, horizontal = 8.dp) // Espaciado alrededor del botón
            .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
            .background(Color.White) // Fondo blanco del botón
            .clickable {
                // Abrir enlace en el navegador
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.papalote.org.mx/actividades/"))
                context.startActivity(intent)
            }, // Acción al hacer clic
        contentAlignment = Alignment.Center // Centrar el texto en el botón
    ) {
        Text(
            text = "¡NOTICIAS!",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary, // Color del texto
            modifier = Modifier.padding(vertical = 16.dp), // Espaciado interno del botón
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RatingStars(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp) // Espaciado entre estrellas
    ) {
        (1..5).forEach { starIndex ->
            Icon(
                imageVector = Icons.Filled.Star, // Usamos el mismo ícono para todas
                contentDescription = "Star $starIndex",
                tint = if (starIndex <= rating) Color(0xFFFFD700) else Color.Black, // Dorado para seleccionadas, negro para el resto
                modifier = Modifier
                    .size(32.dp) // Tamaño de las estrellas
                    .clickable { onRatingChanged(starIndex) } // Cambiar calificación al hacer clic
            )
        }
    }
}

@Composable
fun ObrasDeInteresSection() {
    var obras by remember { mutableStateOf<List<Obra>>(emptyList()) }
    var selectedObraId by remember { mutableStateOf<Int?>(null) }
    var selectedImageWithDetails by remember { mutableStateOf<ImageWithDetails?>(null) }
    val db = Firebase.firestore
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Cargar datos desde Firestore
        db.collection("Obras").get()
            .addOnSuccessListener { result ->
                val tempObras = result.map { obraDocument ->
                    val obraId = obraDocument.getLong("id")?.toInt() ?: 0
                    val obraTitle = obraDocument.getString("title") ?: ""
                    val thumbnailImageResName = obraDocument.getString("thumbnailImageRes") ?: ""
                    val thumbnailImageRes = context.resources.getIdentifier(
                        thumbnailImageResName, "drawable", context.packageName
                    )

                    // Cargar subcolección "Images"
                    val imagesWithDescriptions = mutableListOf<ImageWithDetails>()
                    db.collection("Obras")
                        .document(obraDocument.id)
                        .collection("Images")
                        .get()
                        .addOnSuccessListener { imagesResult ->
                            imagesResult.forEach { imageDoc ->
                                val imageResName = imageDoc.getString("imageRes") ?: ""
                                val imageRes = context.resources.getIdentifier(
                                    imageResName, "drawable", context.packageName
                                )
                                val description = imageDoc.getString("description") ?: ""
                                val title = imageDoc.getString("title") ?: ""
                                imagesWithDescriptions.add(
                                    ImageWithDetails(
                                        imageRes = imageRes,
                                        description = description,
                                        title = title
                                    )
                                )
                            }
                        }

                    Obra(
                        id = obraId,
                        title = obraTitle,
                        thumbnailImageRes = thumbnailImageRes,
                        imagesWithDescriptions = imagesWithDescriptions // Asociar imágenes
                    )
                }
                obras = tempObras
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5DC)) // Fondo beige del contenedor principal
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp) // Limitar la altura de LazyColumn
        ) {
            // Agrupar elementos en pares para crear filas
            items(obras.chunked(2)) { rowObras ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowObras.forEach { obra ->
                        Card(
                            modifier = Modifier
                                .weight(1f) // Hacer que las tarjetas se ajusten al espacio disponible
                                .aspectRatio(1f) // Asegurar que las tarjetas sean cuadradas
                                .clip(RoundedCornerShape(16.dp)) // Bordes circulares
                                .border(
                                    2.dp,
                                    Color.Black,
                                    RoundedCornerShape(16.dp)
                                ) // Bordes negros
                                .clickable {
                                    selectedObraId = obra.id // Actualizar el ID seleccionado
                                    Log.d(
                                        "ObrasDeInteresSection",
                                        "Selected Obra ID: $selectedObraId"
                                    )
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            // Imagen dentro de la tarjeta
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White) // Fondo blanco dentro de la tarjeta
                            ) {
                                Image(
                                    painter = painterResource(id = obra.thumbnailImageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize() // Hacer que la imagen ocupe todo el espacio
                                        .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                                )
                            }
                        }
                    }

                    // Si la fila tiene menos de 2 elementos, añadir un espacio vacío para balancear
                    if (rowObras.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    selectedObraId?.let { id ->
        val obra = obras.find { it.id == id }
        Dialog(
            onDismissRequest = { selectedObraId = null },
            properties = DialogProperties(usePlatformDefaultWidth = false) // Hacer que el diálogo use todo el ancho
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Ocupa toda la pantalla
                    .background(Color(0xFFF5F5DC)) // Color beige
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Botón para cerrar el diálogo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        IconButton(onClick = { selectedObraId = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    obra?.let {
                        it.imagesWithDescriptions.chunked(2).forEach { imagePair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                imagePair.forEach { imageWithDetails ->
                                    // Contenedor que incluye el título encima de la tarjeta
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(bottom = 8.dp), // Espaciado entre filas
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Título encima de la tarjeta
                                        Text(
                                            text = imageWithDetails.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp) // Separación con la tarjeta
                                        )

                                        // Tarjeta
                                        Card(
                                            modifier = Modifier
                                                .aspectRatio(1f) // Hacer que las tarjetas sean cuadradas
                                                .clip(RoundedCornerShape(16.dp)) // Bordes circulares
                                                .border(
                                                    2.dp,
                                                    Color.Black,
                                                    RoundedCornerShape(16.dp)
                                                ) // Bordes negros
                                                .clickable {
                                                    selectedImageWithDetails = imageWithDetails
                                                },
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevación usando CardDefaults
                                        ) {
                                            // Fondo blanco de la tarjeta
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.White) // Fondo blanco de la tarjeta
                                            ) {
                                                Image(
                                                    painter = painterResource(
                                                        id = imageWithDetails.imageRes.takeIf { it != 0 }
                                                            ?: R.drawable.ic_lock
                                                    ),
                                                    contentDescription = imageWithDetails.description,
                                                    modifier = Modifier
                                                        .fillMaxSize() // Imagen llena toda la tarjeta
                                                        .clip(RoundedCornerShape(16.dp)), // Bordes redondeados
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }
                                }
                                // Relleno vacío si hay solo una imagen en la fila
                                if (imagePair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedImageWithDetails?.let { image ->
        var rating by remember { mutableStateOf(0) } // Estado para la calificación

        Dialog(
            onDismissRequest = { selectedImageWithDetails = null },
            properties = DialogProperties(usePlatformDefaultWidth = false) // El diálogo ocupa toda la pantalla
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // El diálogo ocupa toda la pantalla
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(0.dp) // Sin esquinas redondeadas
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Scroll vertical
                        .padding(16.dp)
                ) {
                    // Botón para cerrar el diálogo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        IconButton(onClick = { selectedImageWithDetails = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Imagen y estrellas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp) // Espacio entre la tarjeta y el borde izquierdo
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(120.dp) // Tarjeta más pequeña
                                    .aspectRatio(1f) // Proporción cuadrada
                                    .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp)), // Bordes negros
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = image.imageRes.takeIf { it != 0 } ?: R.drawable.ic_lock
                                    ),
                                    contentDescription = image.description,
                                    modifier = Modifier
                                        .fillMaxSize() // Imagen llena toda la tarjeta
                                        .clip(RoundedCornerShape(16.dp)), // Bordes redondeados
                                    contentScale = ContentScale.Crop // Ajuste proporcional para llenar el espacio
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre la imagen y las estrellas

                            // Calificación con estrellas y botón
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RatingStars(
                                    rating = rating,
                                    onRatingChanged = { newRating -> rating = newRating }
                                )
                                // Botón debajo de las estrellas
                                Button(
                                    onClick = { /* Acción para mostrar el mapa interactivo */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(text = "Ver en mapa interactivo")
                                }
                            }
                        }
                    }

                    // Descripción del texto debajo de la imagen
                    Text(
                        text = image.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    // Espaciador para empujar elementos hacia abajo
                    Spacer(modifier = Modifier.weight(1f))
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




