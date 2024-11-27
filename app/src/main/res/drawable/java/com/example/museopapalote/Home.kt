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
import androidx.navigation.NavController


@Composable
fun Home(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC))
    ) {
        Scaffold(
            topBar = { TopBar(navController) },
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
            }
        }
    }
}

@Composable
fun TopBar(navController: NavController) { // Recibe NavController como parámetro
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
                    .clickable {
                        navController.navigate("profile") // Navega a la pantalla de perfil
                    },
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
        contentAlignment = Alignment.Center // Centrar el contenido del Box
    ) {
        // Imagen encima de todo
        Image(
            painter = painterResource(id = R.drawable.tu_imagen), // Reemplaza con tu imagen
            contentDescription = "Fondo Noticias", // Descripción accesible
            contentScale = ContentScale.Crop, // Ajuste del contenido (opcional)
            modifier = Modifier
                .fillMaxWidth() // O ajusta el tamaño según necesites
                .height(200.dp) // Ajusta la altura según el diseño
        )
    }
}


fun saveToFavorites(obra: Obra, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val userFavoritesRef = db.collection("Users").document(userId).collection("Favorites")

    val favoriteData = mapOf(
        "id" to obra.id,
        "title" to obra.title,
        "thumbnailImageRes" to obra.thumbnailImageRes
    )

    userFavoritesRef.document(obra.id.toString()).set(favoriteData)
        .addOnSuccessListener {
            Log.d("Favorites", "Obra ${obra.title} added to favorites.")
        }
        .addOnFailureListener { e ->
            Log.e("Favorites", "Error adding obra to favorites", e)
        }
}


@Composable
fun RatingStars(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating Star $index",
                tint = if (index <= rating) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(index) }
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
    val userId = FirebaseAuth.getInstance().currentUser?.uid

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
                                val visitado = imageDoc.getBoolean("visitado") ?: false

                                // Leer el rating del usuario si existe
                                var userRating = 0
                                userId?.let { user ->
                                    db.collection("Users").document(user).collection("RatedImages")
                                        .document(title)
                                        .get()
                                        .addOnSuccessListener { userRatingDoc ->
                                            userRating =
                                                userRatingDoc.getLong("rating")?.toInt() ?: 0
                                        }
                                }

                                imagesWithDescriptions.add(
                                    ImageWithDetails(
                                        imageRes = imageRes,
                                        description = description,
                                        title = title,
                                        rating = userRating, // Asignar el rating del usuario
                                        visitado = visitado // Asignar el campo `visitado`
                                    )
                                )
                            }
                        }

                    Obra(
                        id = obraId,
                        title = obraTitle,
                        thumbnailImageRes = thumbnailImageRes,
                        imagesWithDescriptions = imagesWithDescriptions
                    )
                }
                obras = tempObras
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5DC))
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 750.dp)
        ) {
            items(obras.chunked(2)) { rowObras ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowObras.forEach { obra ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        2.dp,
                                        Color.Black,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        selectedObraId = obra.id
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White)
                                ) {
                                    Image(
                                        painter = painterResource(id = obra.thumbnailImageRes),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(16.dp))
                                    )
                                }
                            }
                        }
                    }
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
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(bottom = 8.dp), // Espaciado entre filas
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = imageWithDetails.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp) // Separación con la tarjeta
                                        )

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
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                        ) {
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
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(16.dp)), // Bordes redondeados
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }
                                }
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
        var rating by remember { mutableStateOf(image.rating) }
        var isVisited by remember { mutableStateOf(image.visitado) }

        // Actualizar el estado "Visitado" en Firebase
        fun updateVisitedStatus(newStatus: Boolean) {
            userId?.let { user ->
                db.collection("Users").document(user).collection("RatedImages")
                    .document(image.title)
                    .update("visitado", newStatus)
                    .addOnSuccessListener {
                        Log.d(
                            "Firebase",
                            "Estado 'visitado' actualizado correctamente a $newStatus para ${image.title}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error al actualizar el estado 'visitado'", e)
                    }
            }
        }

        Dialog(
            onDismissRequest = { selectedImageWithDetails = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(0.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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

                    // Imagen y calificación
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = image.imageRes.takeIf { it != 0 } ?: R.drawable.ic_lock
                                    ),
                                    contentDescription = image.description,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Rating
                                RatingStars(
                                    rating = rating,
                                    onRatingChanged = { newRating ->
                                        rating = newRating
                                        image.rating = newRating

                                        // Guardar la calificación en Firebase
                                        userId?.let { user ->
                                            val ratedImageData = mapOf(
                                                "rating" to newRating,
                                                "title" to image.title,
                                                "description" to image.description,
                                                "imageRes" to image.imageRes,
                                                "visitado" to isVisited
                                            )

                                            db.collection("Users").document(user).collection("RatedImages")
                                                .document(image.title)
                                                .set(ratedImageData)
                                                .addOnSuccessListener {
                                                    Log.d("Firebase", "Rating guardado correctamente para el usuario $user")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("Firebase", "Error al guardar el rating", e)
                                                }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Descripción
                    Text(
                        text = image.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkmark de "Visitado"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            isVisited = !isVisited
                            updateVisitedStatus(isVisited)
                        }
                    ) {
                        Checkbox(
                            checked = isVisited,
                            onCheckedChange = {
                                isVisited = it
                                updateVisitedStatus(it)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Visitado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

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
    val description: String,
    var rating: Int = 0,
    var visitado: Boolean
)