package com.example.museopapalote
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Page3(navController: NavHostController) {
    Button(onClick = { navController.navigate("Home") }) {
        Text("Go to Home")
    }
}
