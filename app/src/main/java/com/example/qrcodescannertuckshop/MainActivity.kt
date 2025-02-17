package com.example.qrcodescannertuckshop

import android.os.Bundle
import android.util.Log
import android.media.MediaPlayer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.qrcodescannertuckshop.ui.theme.QRCodeScannerTuckshopTheme
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.GsonFactory
import com.google.api.client.googleapis.auth.oauth2.GoogleAccountCredential
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task



// OAuth2 Authentication
private const val REQUEST_ACCOUNT_PICKER = 1000
private const val REQUEST_AUTHORIZATION = 1001
private const val SCOPES = "https://www.googleapis.com/auth/spreadsheets"

class MainActivity : ComponentActivity() {
    private lateinit var credential: GoogleAccountCredential
    private lateinit var sheetsService: Sheets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeScannerTuckshopTheme {
                AppNavigator(onExitApp = { finish() })
            }
        }

        // Initialize Google Sheets API
        initializeGoogleSheetsApi()
    }

    class MainActivity : ComponentActivity() {
        private lateinit var credential: GoogleAccountCredential
        private lateinit var sheetsService: Sheets

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                QRCodeScannerTuckshopTheme {
                    AppNavigator(onExitApp = { finish() })
                }
            }

            // Initialize Google Sheets API
            initializeGoogleSheetsApi()
        }

        private fun initializeGoogleSheetsApi() {
            val transport = AndroidHttp.newCompatibleTransport()
            val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

            // Load the credentials from the raw folder
            val inputStream = resources.openRawResource(R.raw.client_secret) // Use the correct filename here
            val reader = InputStreamReader(inputStream)
            val credential = GoogleAccountCredential.usingOAuth2(this, listOf(SCOPES))

            // Initialize Sheets API
            sheetsService = Sheets.Builder(transport, jsonFactory, credential)
                .setApplicationName("QRCode Scanner Tuckshop")
                .build()

            // You can now use sheetsService to interact with Google Sheets
        }
    }


    private fun initializeGoogleSheetsApi() {
        // Request the user's Google account for authentication
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(SCOPES))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            // If the account is already signed in, proceed
            credential = GoogleAccountCredential.usingOAuth2(this, listOf(SCOPES))
                .setSelectedAccount(account.account)
            initializeSheetsService()
        } else {
            startActivityForResult(googleSignInClient.signInIntent, REQUEST_ACCOUNT_PICKER)
        }
    }

    private fun initializeSheetsService() {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
        sheetsService = Sheets.Builder(transport, jsonFactory, credential)
            .setApplicationName("QRCode Scanner Tuckshop")
            .build()
    }

    // Handle authentication result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ACCOUNT_PICKER) {
            val result: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = result.getResult(ApiException::class.java)
                credential = GoogleAccountCredential.usingOAuth2(this, listOf(SCOPES))
                    .setSelectedAccount(account.account)
                initializeSheetsService()
            } catch (e: ApiException) {
                Log.w("Google Sheets", "Sign-in failed", e)
            }
        }
    }

    // Add data to Google Sheets
    fun addDataToSheet(barcodeValue: String) {
        val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA" // Your Google Sheet ID
        val range = "Sheet1!A1" // Set the range to add the data
        val values = listOf(
            listOf(barcodeValue) // Add the scanned barcode value here
        )

        val body = ValueRange().setValues(values)
        val request = sheetsService.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption("RAW")

        request.execute()
    }
}

@Composable
fun AppNavigator(onExitApp: () -> Unit) {
    var currentScreen by remember { mutableStateOf("Greeting") }

    LaunchedEffect(Unit) {
        delay(3000)
        currentScreen = "MainMenu"
    }

    when (currentScreen) {
        "Greeting" -> GreetingScreen()
        "MainMenu" -> MainMenuScreen(
            onSalesAndTransactions = { currentScreen = "SalesAndTransactions" },
            onInventory = { currentScreen = "Inventory" },
            onSettings = { currentScreen = "Settings" },
            onExitApp = onExitApp
        )
        "SalesAndTransactions" -> SalesAndTransactionsScreen(
            onScanQRCode = { currentScreen = "CameraScreen:SalesAndTransactions" },
            onBack = { currentScreen = "MainMenu" }
        )
        "Inventory" -> InventoryScreen(
            onScanItemToAddToInventory = { currentScreen = "CameraScreen:Inventory" },
            onBack = { currentScreen = "MainMenu" }
        )
        "Settings" -> SettingsScreen(onBack = { currentScreen = "MainMenu" })
        "CameraScreen:SalesAndTransactions" -> CameraScreen(previousScreen = "SalesAndTransactions", onBack = { currentScreen = "SalesAndTransactions" })
        "CameraScreen:Inventory" -> CameraScreen(previousScreen = "Inventory", onBack = { currentScreen = "Inventory" })
    }
}

@Composable
fun CameraScreen(previousScreen: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var barcodeValue by remember { mutableStateOf("") }
    var hasScanned by remember { mutableStateOf(false) }

    val mediaPlayer = remember { MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                barcodeValue = "Camera permission denied"
                hasScanned = true
            }
        }
    )

    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(barcodeValue) {
        if (hasScanned && barcodeValue.isNotEmpty()) {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            if (previousScreen == "Inventory") {
                addDataToSheet(barcodeValue) // Add the barcode to Google Sheets if it's from Inventory
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasScanned) {
            LaunchedEffect(barcodeValue) {
                delay(3000) // Wait for 3 seconds before clearing the message
                barcodeValue = "" // Clear the barcode message
                hasScanned = false
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Scanned Code: $barcodeValue",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    val cameraProvider = cameraProviderFuture.get()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                val scanner = BarcodeScanning.getClient()
                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            barcode.rawValue?.let {
                                                barcodeValue = it
                                                hasScanned = true
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        barcodeValue = "Scan Failed"
                                        hasScanned = true
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            }
                        }
                    )

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        )

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Back", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
