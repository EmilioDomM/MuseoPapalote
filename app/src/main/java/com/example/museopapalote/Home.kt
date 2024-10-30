package com.example.museopapalote
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Home(navController: NavHostController) {

    Row {
        Button(onClick = { navController.navigate("page1") }) {
            Text("Go to Page 1")
        }

        Button(onClick = { navController.navigate("page2") }) {
            Text("Go to Page 2")
        }

        Button(onClick = { navController.navigate("page3") }) {
            Text("Go to Page 3")
        }

        Button(onClick = { navController.navigate("Home") }) {
            Text("Go to Home")
        }
    }


}