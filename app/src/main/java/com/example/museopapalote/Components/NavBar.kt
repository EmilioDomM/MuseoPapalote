package com.example.landingpage.components.NavBar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.museopapalote.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = "Map",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Map", fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "map",
            onClick = {
                navController.navigate("map") {
                    popUpTo("map") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_analytics),
                    contentDescription = "QR",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("QR", fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "qr",
            onClick = {
                navController.navigate("qr") {
                    popUpTo("qr") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = navController.currentDestination?.route == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo("profile") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}


