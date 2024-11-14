import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.museopapalote.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QR(navController: NavHostController) {
    val context = LocalContext.current
    var scannedText by remember { mutableStateOf("Escanea un código QR") }
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result: ScanIntentResult? ->
        if (result?.contents != null) {
            scannedText = "Valor Escaneado: ${result.contents}"
        } else {
            Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                scanLauncher.launch(ScanOptions().setPrompt("Escanear código QR"))
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB7DD6A)) // Pantone color equivalent
    ) {
        // Back Arrow Icon
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_black),
            contentDescription = "Back Arrow",
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .align(Alignment.TopStart)
                .clickable { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Scan QR Button at the top
            Button(
                onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Escanear QR", color = Color.White)
            }

            // Display scanned text at the bottom center
            Text(
                text = scannedText,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = Color.DarkGray.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
        }
    }
}
