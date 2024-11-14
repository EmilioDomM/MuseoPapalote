package com.example.museopapalote
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Map(navController: NavHostController) {
    Button(onClick = { navController.navigate("qr") }) {
        Text("Go to QR")
    }
}


