package com.example.museopapalote
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Page1(navController: NavHostController) {
    Button(onClick = { navController.navigate("page2") }) {
        Text("Go to Page 2")
    }
}
