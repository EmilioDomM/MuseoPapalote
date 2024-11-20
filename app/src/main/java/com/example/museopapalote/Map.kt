package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// Data class for clickable areas
data class ClickableArea(
    val x: Float, // Relative x position (0f - 1f)
    val y: Float, // Relative y position (0f - 1f)
    val label: String
)

@Composable
fun Map(navController: NavHostController) {
    var selectedFloor by remember { mutableStateOf(1) }
    var scale by remember { mutableStateOf(1.3f) } // Persist zoom across floors
    var offset by remember { mutableStateOf(Offset.Zero) } // Persist pan across floors

    val floorImages = listOf(
        R.drawable.first_floor,
        R.drawable.second_floor,
        R.drawable.third_floor
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFD3E05D))) {
        // Interactive map layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f) // Maintain zoom limits
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
                Image(
                    painter = painterResource(floorImages[selectedFloor - 1]),
                    contentDescription = "Floor $selectedFloor Map",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Floor selector buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) { floor ->
                Button(
                    onClick = { selectedFloor = floor + 1 }, // Only change floor, keep zoom and offset
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFloor == floor + 1) Color(0xFF4CAF50) else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Floor ${floor + 1}",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}