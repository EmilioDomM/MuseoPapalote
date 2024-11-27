package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// Data class for clickable areas
data class ClickableArea(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val label: String,
    val color: Color
)

data class Floor(
    val imageRes: Int,
    val clickableAreas: List<ClickableArea>
)

@Composable
fun Map(navController: NavHostController) {
    var selectedFloor by remember { mutableStateOf(1) }
    var scale by remember { mutableStateOf(1.7f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var selectedLabel by remember { mutableStateOf("Toca el mapa para ver las Zonas") }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var selectedColor by remember { mutableStateOf(Color.Transparent) }

    val floors = listOf(
        Floor(
            imageRes = R.drawable.first_floor,
            clickableAreas = listOf(
                ClickableArea(
                    x = 0.4f, y = 0.45f, width = 0.25f, height = 0.05f,
                    label = "Paquetería, sillas de ruedas y carreolas",
                    color = Color.Black
                ),
                ClickableArea(
                    x = 0.53f, y = 0.52f, width = 0.175f, height = 0.04f,
                    label = "Área de Alimentos",
                    color = Color.Black
                )
            )
        ),
        Floor(
            imageRes = R.drawable.second_floor,
            clickableAreas = listOf(
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = 0.5f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "Acceso Principal",
                    color = Color.Black
                ),
                ClickableArea(
                    x = 0.42f, y = 0.41f, width = 0.07f, height = 0.015f,
                    label = "Acceso IMAX",
                    color = Color.Black
                ),
                ClickableArea(
                    x = 0.4f, y = 0.485f, width = 0.078f, height = 0.02f,
                    label = "Tienda",
                    color = Color.Black
                ),
                ClickableArea(
                    x = 0.57f, y = 0.48f, width = 0.22f, height = 0.05f,
                    label = "Pertenezco",
                    color = Color(0xFF37A841)
                ),
                ClickableArea(
                    x = 0.52f, y = 0.53f, width = 0.22f, height = 0.06f,
                    label = "Comunico",
                    color = Color(0xFF006E9F)
                ),
                ClickableArea(
                    x = 0.528f, y = 0.43f, width = 0.1f, height = 0.05f,
                    label = "Sala de exposiciones",
                    color = Color.Black
                )
            )
        ),
        Floor(
            imageRes = R.drawable.third_floor,
            clickableAreas = listOf(
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = -10f, y = 0.41f, width = 0.07f, height = 0.02f,
                    label = "secreto",
                    color = Color.Transparent
                ),
                ClickableArea(
                    x = 0.3f, y = 0.407f, width = 0.1f, height = 0.065f,
                    label = "Megapantalla IMAX",
                    color = Color.Black
                ),
                ClickableArea(
                    x = 0.56f, y = 0.445f, width = 0.1f, height = 0.06f,
                    label = "Comprendo",
                    color = Color(0xFF853694)
                ),
                ClickableArea(
                    x = 0.63f, y = 0.48f, width = 0.2f, height = 0.05f,
                    label = "Expreso",
                    color = Color(0xFFF4821F)
                ),
                ClickableArea(
                    x = 0.56f, y = 0.5f, width = 0.08f, height = 0.05f,
                    label = "Soy",
                    color = Color(0xFFDC1E36)
                ),
                ClickableArea(
                    x = 0.6f, y = 0.55f, width = 0.11f, height = 0.04f,
                    label = "Soy",
                    color = Color(0xFFDC1E36)
                ),
                ClickableArea(
                    x = 0.71f, y = 0.54f, width = 0.08f, height = 0.04f,
                    label = "Pequeño",
                    color = Color(0xFF009BA6)
                )
            )
        )
    )


    val currentFloor = floors.getOrNull(selectedFloor - 1)

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFD3E05D))) {
        // Persistent top card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(0.dp, 10.dp)
                .align(Alignment.TopStart),
            contentAlignment = Alignment.TopCenter
        ) {
            if (selectedLabel.isNotEmpty()) {
                val currentArea = currentFloor?.clickableAreas?.find { it.label == selectedLabel }
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0XFFF5F5DC),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 8.dp,
                            color = selectedColor, // Dynamic border color
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = selectedLabel,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // Interactive map layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        offset += pan
                    }
                }
        ) {
            Box(
                modifier = Modifier.graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
            ) {
                if (currentFloor != null) {
                    // Render the floor image
                    Image(
                        painter = painterResource(currentFloor.imageRes),
                        contentDescription = "Floor $selectedFloor Map",
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned { coordinates ->
                                imageSize = coordinates.size
                            }
                    )

                    // Render the clickable areas
                    currentFloor.clickableAreas.forEach { area ->
                        if (imageSize.width > 0 && imageSize.height > 0) {
                            val areaX = area.x * imageSize.width
                            val areaY = area.y * imageSize.height
                            val areaWidth = area.width * imageSize.width
                            val areaHeight = area.height * imageSize.height

                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = with(LocalDensity.current) { areaX.toDp() },
                                        y = with(LocalDensity.current) { areaY.toDp() }
                                    )
                                    .size(
                                        width = with(LocalDensity.current) { areaWidth.toDp() },
                                        height = with(LocalDensity.current) { areaHeight.toDp() }
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures {
                                            selectedLabel = area.label
                                            selectedColor = area.color
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }

        // Floor selector buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(0.dp, -75.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    selectedFloor = 1
                    selectedLabel = ""
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFloor == 1) Color(0xFF4CAF50) else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Planta Baja",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Button(
                onClick = {
                    selectedFloor = 2
                    selectedLabel = ""
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFloor == 2) Color(0xFF4CAF50) else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sótano 1",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Button(
                onClick = {
                    selectedFloor = 3
                    selectedLabel = ""
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFloor == 3) Color(0xFF4CAF50) else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sótano 2",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
