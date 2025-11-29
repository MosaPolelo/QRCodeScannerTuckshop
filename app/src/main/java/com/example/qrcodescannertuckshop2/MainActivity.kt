package com.example.qrcodescannertuckshop2

// CameraX and AndroidView

// ML Kit for barcode scanning

// KEEP these:


//import androidx.webkit.ProfileStore
import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.qrcodescannertuckshop.ui.theme.QRCodeScannerTuckshopTheme
import com.example.qrcodescannertuckshop2.auth.UserManager
import com.example.qrcodescannertuckshop2.data.local.AppDb
import com.example.qrcodescannertuckshop2.data.local.LocalUserRepository
import com.example.qrcodescannertuckshop2.data.local.ProfileStore
import com.example.qrcodescannertuckshop2.data.local.UserEntity
import com.example.qrcodescannertuckshop2.data.local.UserRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.random.Random


enum class UserRole { ADMIN, CASHIER }

data class AppUser(
    val uid: String,
    val username: String,
    val role: UserRole,
    val photoPath: String? = null
)


private const val CAMERA_PERMISSION_REQUEST_CODE = 101


var currentUserRole: UserRole? = null


suspend fun extractTextFromImage(imagePath: String): String? {
    return try {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val image = InputImage.fromBitmap(bitmap, 0)

        val recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(image).await()

        result.text
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let {
                            onBarcodeDetected(it)
                        }
                    }
                }
                .addOnFailureListener {
                    // Ignore
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}


@Composable
fun FadeInImage(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(bitmap) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        modifier = modifier.alpha(alphaAnim.value)
    )
}


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuckshopTopBar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    onSwitchAccount: () -> Unit = {},
    onTakeProfilePhoto: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    avatarPath: String? = null
) {
    val fbUser by UserManager.currentUser.collectAsState()
    val ctx = LocalContext.current
    val localPhotoPath by UserManager.localPhotoPath.collectAsState()

    val savedName by ProfileStore.nameFlow(ctx).collectAsState(initial = null)
    val savedRole by ProfileStore.roleFlow(ctx).collectAsState(initial = null)
    val savedPhoto by ProfileStore.photoPathFlow(ctx).collectAsState(initial = null)

    var menuOpen by remember { mutableStateOf(false) }

    // Prefer Firebase name, else persisted name, else email, else Guest
    val name = fbUser?.displayName
        ?: savedName
        ?: fbUser?.email?.substringBefore('@')
        ?: "Guest"

    // Prefer persisted/local photo path, else Firebase photoUrl
    val photoToShow: String? =
        avatarPath
            ?: localPhotoPath
            ?: savedPhoto
            ?: fbUser?.photoUrl?.toString()

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )

                IconButton(
                    onClick = {
                        onSwitchAccount()
                        onAvatarClick()
                        menuOpen = true
                    }
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                }

                // Avatar (falls back to letter circle if no photo)
                if (!photoToShow.isNullOrBlank()) {
                    val model: Any =
                        if (photoToShow.startsWith("/")) File(photoToShow) else photoToShow

                    Image(
                        painter = rememberAsyncImagePainter(model),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = name.firstOrNull()?.uppercase() ?: "G",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Switch account") },
                        onClick = {
                            menuOpen = false
                            onSwitchAccount()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take profile photo") },
                        onClick = {
                            menuOpen = false
                            onTakeProfilePhoto()
                        }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Sign out") },
                        onClick = {
                            menuOpen = false
                            onSignOut()
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun ProfilePhotoDialog(
    onDismiss: () -> Unit,
    onBitmapReady: (android.graphics.Bitmap) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Camera capture (thumbnail)
    val takePhotoLauncher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
        ) { bmp: android.graphics.Bitmap? ->
            if (bmp != null) onBitmapReady(bmp)
            onDismiss()
        }

    // Gallery picker
    val pickImageLauncher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.GetContent()
        ) { uri: android.net.Uri? ->
            if (uri != null) {
                val bmp = runCatching {
                    context.contentResolver.openInputStream(uri)?.use {
                        android.graphics.BitmapFactory.decodeStream(it)
                    }
                }.getOrNull()
                if (bmp != null) onBitmapReady(bmp)
            }
            onDismiss()
        }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        androidx.compose.material3.Surface(shape = androidx.compose.material3.MaterialTheme.shapes.large) {
            androidx.compose.foundation.layout.Column(
                modifier = androidx.compose.ui.Modifier.padding(24.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "Update profile photo",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                androidx.compose.foundation.layout.Spacer(Modifier.height(12.dp))
                androidx.compose.material3.Button(onClick = { takePhotoLauncher.launch(null) }) {
                    androidx.compose.material3.Text("Take photo")
                }
                androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
                androidx.compose.material3.Button(onClick = { pickImageLauncher.launch("image/*") }) {
                    androidx.compose.material3.Text("Choose from gallery")
                }
                androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
                androidx.compose.material3.TextButton(onClick = onDismiss) {
                    androidx.compose.material3.Text("Cancel")
                }
            }
        }
    }
}


class MainActivity : ComponentActivity() {
   // private lateinit var signInClient: SignInClient
    private lateinit var credential: GoogleAccountCredential
    private lateinit var sheetsService: Sheets
    private lateinit var credentialManager: CredentialManager
    private var signInTried = false
    private var chooserShown = false



    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
    }


    fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }


    fun getSheetsService(context: Context): Sheets? {
        return try {
            Log.d("GoogleSheets", "🔍 Attempting to get Sheets service...")

            val inputStream = context.resources.openRawResource(R.raw.service_account)
            Log.d("GoogleSheets", "✅ Successfully opened service_account.json")

            val credentials = ServiceAccountCredentials.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets"))

            // ✅ Wrap credentials in HttpCredentialsAdapter
            val requestInitializer = HttpCredentialsAdapter(credentials)

            Log.d("GoogleSheets", "🚀 Building Sheets service...")

            return Sheets.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
            )
                .setApplicationName("QRCode Scanner Tuckshop")
                .build()

        } catch (e: Exception) {
            Log.e("GoogleSheets", "🚨 Exception during Sheets service init: ${e.message}", e)
            null
        }
    }

    override fun onStart() {                      // NEW
        super.onStart()
        initializeGoogleSignIn()                  // one-shot guarded by signInTried
    }


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserManager.start()
        initializeGoogleSignIn()

        val isTablet = resources.getBoolean(R.bool.isTablet)
        if (!isTablet) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("CameraPermission", "✅ Camera permission granted")
            } else {
                Log.d("CameraPermission", "❌ Camera permission denied")
            }
        }

        val service: Sheets? = getSheetsService(context = this)
        var currentUser: AppUser? = null

        setContent {
            QRCodeScannerTuckshopTheme {

                val fbUser: FirebaseUser? by UserManager.currentUser.collectAsState(initial = null)

                var currentScreen by remember {
                    mutableStateOf(
                        if (fbUser != null) Routes.POS_MENU else Routes.LOGIN
                    )
                }

                LaunchedEffect(fbUser) {
                    currentScreen = if (fbUser != null) Routes.POS_MENU else Routes.LOGIN
                }

                val service: Sheets? = getSheetsService(context = this)

                service?.let { sheetsService ->
                    AppNavigator(
                        service = sheetsService,
                        onExitApp = { finish() },

                        currentScreen = currentScreen,
                        setCurrentScreen = { screen -> currentScreen = screen },

                        currentUser = fbUser, // just for routing/navigation
                        setCurrentUser = {},  // FirebaseUser is not manually set, so skip this
                        launchAccountChooser = { launchAccountChooser() }
                    )
                }
            }
        }




        initializeGoogleSignIn()
        credentialManager = CredentialManager.create(this)
    }


    fun initializeGoogleSignIn() {
        if (signInTried) return
        signInTried = true

        val credentialManager = CredentialManager.create(this)

        // Try silent sign-in first
        val silentOption = GetSignInWithGoogleOption.Builder(
            getString(R.string.default_web_client_id) // from strings.xml (wired by google-services.json)
        ).build()

        val silentReq = GetCredentialRequest.Builder()
            .addCredentialOption(silentOption)
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp: GetCredentialResponse = credentialManager.getCredential(
                    request = silentReq,
                    context = this@MainActivity
                )
                handleCredentialResponse(resp)
                return@launch
            } catch (_: Exception) {
                // no cached account or multiple accounts -> fall back to chooser
            }

            // 2) One-shot chooser (guarded)
            if (chooserShown) return@launch       // NEW
            chooserShown = true                   // NEW

            // Show account chooser
            try {
                val chooserOption = GetSignInWithGoogleOption.Builder(
                    getString(R.string.default_web_client_id)
                ).build()

                val chooserReq = GetCredentialRequest.Builder()
                    .addCredentialOption(chooserOption)
                    .build()

                val resp2: GetCredentialResponse = credentialManager.getCredential(
                    request = chooserReq,
                    context = this@MainActivity
                )
                handleCredentialResponse(resp2)
            } catch (_: Exception) {
                // user cancelled or no Google account available (optional: toast/snackbar)
            }
        }
    }

    private fun launchAccountChooser() {
        val credentialManager = CredentialManager.create(this)
        val option = GetSignInWithGoogleOption.Builder(getString(R.string.default_web_client_id))
            .build() // no setAutoSelectEnabled
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp = credentialManager.getCredential(
                    request = request,
                    context = this@MainActivity
                )
                handleCredentialResponse(resp) // your existing helper
            } catch (_: Exception) {
                // user cancelled / no account — optional toast/snackbar on Main
            }
        }
    }




    // Extract ID token and sign into Firebase so UI can show the real name/photo
    private suspend fun firebaseSignInWithIdToken(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update the user shown in the TopBar
                // UserManager listens to FirebaseAuth already; no extra call needed
                Log.d("GoogleSignIn", "Firebase sign-in success")
            } else {
                Log.e("GoogleSignIn", "Firebase sign-in error: ${task.exception?.message}")
            }
        }
    }

    private suspend fun handleGoogleIdToken(cred: Credential) {
        val googleIdCred = GoogleIdTokenCredential.createFrom(cred.data)
        val idToken = googleIdCred.idToken
        withContext(Dispatchers.Main) {
            firebaseSignInWithIdToken(idToken)
        }
    }

    private suspend fun handleCredentialResponse(resp: GetCredentialResponse) {
        val cred: Credential = resp.credential
        if (cred is CustomCredential &&
            cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCred = GoogleIdTokenCredential.createFrom(cred.data)
            val idToken: String = googleCred.idToken

            val auth = FirebaseAuth.getInstance()
            val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)

            val result: AuthResult = auth.signInWithCredential(firebaseCred).await()
            val fbUser: FirebaseUser? = result.user

            withContext(Dispatchers.Main) {
                // Update UI state via UserManager
                UserManager.onGoogleSignIn(fbUser)
            }

            // Persist a default profile for Settings + TopBar
            val friendly: String = fbUser?.displayName
                ?: fbUser?.email?.substringBefore('@')
                ?: "User"

            withContext(Dispatchers.IO) {
                if (fbUser != null) {
                    ProfileStore.saveActive(
                        ctx = this@MainActivity,
                        name = friendly,
                        role = "CASHIER" ,
                        photoPath = fbUser.photoUrl?.toString()// default; you'll change when ManageUsers picks a role
                    )
                }
            }
        } else {
            Log.w("GoogleSignIn", "Unsupported credential type: ${cred::class.java}")
        }
    }



    fun checkAndRequestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> {
                Log.d("CameraPermission", "✅ Camera permission already granted")
                // Optional: Automatically go to scanner
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Log.w("CameraPermission", "ℹ️ Explain to user why the permission is needed")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


    private fun handleSignInResult(idToken: String) {
        Log.d("GoogleSignIn", "Sign-In Successful: $idToken")

        // ✅ 2a. Sign in to Firebase so we can get displayName/photo/email
        val firebaseCred = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        com.google.firebase.auth.FirebaseAuth.getInstance()
            .signInWithCredential(firebaseCred)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        // ✅ 2b. Publish to our app-wide state
                        UserManager.onGoogleSignIn(user)
                    }
                } else {
                    Log.e("GoogleSignIn", "Firebase sign-in failed", task.exception)
                }
            }

        // ✅ 2c. Keep your Google Sheets stuff as you had it
        credential = GoogleAccountCredential.usingOAuth2(
            this, listOf("https://www.googleapis.com/auth/spreadsheets")
        )
        initializeSheetsService()
    }


    private fun initializeSheetsService() {
        val transport = NetHttpTransport()
        val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
        sheetsService = Sheets.Builder(transport, jsonFactory, credential)
            .setApplicationName("QRCode Scanner Tuckshop")
            .build()
    }
}

fun writeToGoogleSheets(
    barcode: String,
    name: String,
    price: String,
    quantity: Int,
    date: String,
    time: String,
    service: Sheets,
    coroutineScope: CoroutineScope,
    imagePath: String?
)
 {
    coroutineScope.launch {
        try {
            val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA"
            val rangeToRead = "Sheet1!A:A" // Check only column A for now

            // Read to determine next available row
            val readResult = withContext(Dispatchers.IO) {
                service.spreadsheets().values()
                    .get(spreadsheetId, rangeToRead)
                    .execute()
            }

            val rowCount = readResult.getValues()?.size ?: 0
            val nextRow = rowCount + 1
            val writeRange = "Sheet1!A$nextRow"

            // Multi-column data: Barcode, Name, Timestamp
            val values: List<List<String>> = listOf(
                listOf(barcode, name, price, quantity.toString(), date, time, imagePath ?: "")
            )
            val body = ValueRange().setValues(values)

            // Write the full row
            val result = withContext(Dispatchers.IO) {
                service.spreadsheets().values()
                    .update(spreadsheetId, writeRange, body)
                    .setValueInputOption("RAW")
                    .execute()
            }

            Log.d("GoogleSheets", "✅ Successfully wrote to row $nextRow: ${result.updatedCells} cells updated.")

        } catch (e: Exception) {
            Log.e("GoogleSheets", "❌ Failed to write data: ${e.message}", e)
        }
    }
}

fun List<Any>.getOrNullSafe(index: Int): String? {
    return if (index in indices) this[index].toString() else null
}


suspend fun updateInventoryQuantitiesAfterSale(
    soldItems: List<Product>,
    sheetName: String,
    service: Sheets
) {
    val range = "$sheetName!A2:G"  // A to G columns, skip header
    val inventoryData = withContext(Dispatchers.IO) {
        service.spreadsheets().values()
            .get("1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA", range)
            .execute()
            .getValues() ?: emptyList()
    }

    for ((index, row) in inventoryData.withIndex()) {
        val sheetBarcode = row.getOrNullSafe(0)?.trim() ?: continue

        val matchingItem = soldItems.find { it.barcode.trim() == sheetBarcode }
        if (matchingItem != null) {
            val currentQty = row.getOrNullSafe(3)?.toIntOrNull() ?: 0
            val updatedQty = (currentQty - matchingItem.quantity).coerceAtLeast(0)

            val rowNumber = index + 2 // Skip header row
            val quantityCell = "$sheetName!D$rowNumber"

            val body = ValueRange().setValues(listOf(listOf(updatedQty.toString())))

            try {
                withContext(Dispatchers.IO) {
                    service.spreadsheets().values()
                        .update("1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA", quantityCell, body)
                        .setValueInputOption("RAW")
                        .execute()
                }
                Log.d("GoogleSheets", "✅ Updated $sheetBarcode quantity to $updatedQty at row $rowNumber")
            } catch (e: Exception) {
                Log.e("GoogleSheets", "❌ Failed to update $sheetBarcode: ${e.message}", e)
            }
        }
    }
}



fun updateProductInGoogleSheets(
    updatedProduct: Product,
    date: String,
    time: String,
    service: Sheets,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        try {
            val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA"
            val readRange = "Sheet1!A2:A" // Only the barcodes, skip header

            val response = withContext(Dispatchers.IO) {
                service.spreadsheets().values()
                    .get(spreadsheetId, readRange)
                    .execute()
            }

            val values = response.getValues() ?: return@launch

            val rowIndex = values.indexOfFirst { row -> row.isNotEmpty() && row[0].toString() == updatedProduct.barcode }

            if (rowIndex != -1) {
                val rowToUpdate = rowIndex + 2 // Add 2 (1 for header + 1 for 1-based index)
                val updateRange = "Sheet1!A$rowToUpdate"

                val updatedValues = listOf(
                    listOf(
                        updatedProduct.barcode,
                        updatedProduct.name,
                        updatedProduct.price,
                        updatedProduct.quantity.toString(),
                        date,
                        time,
                        updatedProduct.imagePath ?: ""
                    )
                )

                val body = ValueRange().setValues(updatedValues)

                withContext(Dispatchers.IO) {
                    service.spreadsheets().values()
                        .update(spreadsheetId, updateRange, body)
                        .setValueInputOption("RAW")
                        .execute()
                }

                Log.d("GoogleSheets", "✅ Row $rowToUpdate updated for barcode: ${updatedProduct.barcode}")
            } else {
                Log.w("GoogleSheets", "⚠️ Could not find barcode to update. Falling back to write.")
                // Fallback to insert if not found
                writeToGoogleSheets(
                    barcode = updatedProduct.barcode,
                    name = updatedProduct.name,
                    price = updatedProduct.price,
                    quantity = updatedProduct.quantity,
                    date = date,
                    time = time,
                    service = service,
                    coroutineScope = coroutineScope,
                    imagePath = updatedProduct.imagePath
                )
            }

        } catch (e: Exception) {
            Log.e("GoogleSheets", "❌ Failed to update data: ${e.message}", e)
        }
    }
}



// ✅ Add this anywhere *outside* of a function or class
data class Product(
    val barcode: String,
    val name: String,
    val price: String,
    val imagePath: String?,
    var quantity: Int = 1
)

data class ProductItem(val barcode: String)

fun addProductToGoogleSheets(service: Sheets, product: Product) {
    try {
        val values = listOf(
            listOf(
                product.barcode,
                product.name,
                product.price,
                product.quantity.toString(),
                product.imagePath ?: ""
            )
        )

        val body = ValueRange().setValues(values)
        service.spreadsheets().values()
            .append("1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA", "Products!A:G", body)  // <-- Replace Products!A:E if your sheet is different
            .setValueInputOption("USER_ENTERED")
            .execute()

    } catch (e: Exception) {
        Log.e("SheetInsert", "Failed to add product: ${e.message}")
    }
}

fun updateProductQuantityInGoogleSheets(service: Sheets, barcode: String, newQuantity: Int) {
    try {
        val body = ValueRange().setValues(
            listOf(
                listOf(barcode, newQuantity.toString())
            )
        )
        service.spreadsheets().values()
            .append(
                "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA",  // 🔥 Remember to replace with your actual spreadsheet ID
                "Products!A:G",          // 🔥 Make sure this matches your layout
                body
            )
            .setValueInputOption("USER_ENTERED")
            .execute()
    } catch (e: Exception) {
        Log.e("SheetUpdate", "❌ Failed to update quantity: ${e.message}")
    }
}

fun updateProductsInGoogleSheets(context: Context, productList: List<Product>)
 {
    val service = (context as? MainActivity)?.getSheetsService(context)

    if (service != null) {
        productList.forEach { product ->
            try {
                updateProductQuantityInGoogleSheets(
                    service,
                    product.barcode,
                    product.quantity
                )
                Log.d("SheetUpdate", "✅ Updated ${product.name} to quantity ${product.quantity}")
            } catch (e: Exception) {
                Log.e("SheetUpdate", "❌ Failed to update ${product.name}: ${e.message}")
            }
        }
    } else {
        Log.e("SheetUpdate", "❌ Google Sheets service unavailable")
    }
}

fun addNewProductToGoogleSheets(context: Context, product: Product)
 {
       val service = (context as? MainActivity)?.getSheetsService(context)

    if (service != null) {
        try {
            // Call your update/insert function
            updateProductQuantityInGoogleSheets(
                service,
                product.barcode ,
                product.quantity
            )
            Log.d("SheetUpdate", "✅ Added new product ${product.name} with quantity ${product.quantity}")
        } catch (e: Exception) {
            Log.e("SheetUpdate", "❌ Failed to add new product: ${e.message}")
        }
    } else {
        Log.e("SheetUpdate", "❌ Google Sheets service unavailable for new product")
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerPreview(
    onBarcodeDetected: (String) -> Unit,
    scannerActive: Boolean,
    onPauseScanner: () -> Unit,
    onCameraControlReady: (CameraControl) -> Unit// ✅ new callback
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember { mutableStateOf<PreviewView?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewView = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        LaunchedEffect(previewView) {
            if (previewView == null) return@LaunchedEffect

            val cameraProvider = ProcessCameraProvider.getInstance(context).get()

            val previewUseCase = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView!!.surfaceProvider)
            }

            val barcodeScanner = BarcodeScanning.getClient()

            val analysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null && scannerActive) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    barcodeScanner.process(inputImage)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let { barcodeValue ->
                                    onPauseScanner() // ✅ triggers the parent to disable scanner
                                    onBarcodeDetected(barcodeValue)  // ✅ Pass barcode to caller (SalesScreen)

                                }
                            }
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    analysisUseCase
                )
            } catch (exc: Exception) {
                Log.e("ScannerPreview", "Camera binding failed", exc)
            }
        }
    }
}

@Composable
fun CaptureProfilePhotoScreen(
    onBack: () -> Unit,
    onPhotoSaved: () -> Unit,
    currentUser: AppUser?
) {
    val ctx: Context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher for system camera preview
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        if (bmp != null) {
            val uidForSave = UserManager.currentUser.value?.uid
                ?: "Local:${currentUser?.username ?: "unknown"}"

            coroutineScope.launch {
                val ok = UserManager.updatePhoto(ctx, uidForSave, bmp)
                if (ok) {
                    Toast.makeText(ctx, "Profile photo updated", Toast.LENGTH_SHORT).show()
                    onPhotoSaved()
                    onBack()
                } else {
                    Toast.makeText(ctx, "Couldn't update profile photo", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Capture Profile Photo",
                showBack = true,
                onBack = onBack,
                onTakeProfilePhoto = { cameraLauncher.launch(null) }, // top-right camera icon
                onSignOut = {},
                onSwitchAccount = {},
                onAvatarClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { cameraLauncher.launch(null) }) {
                Text("Take Profile Photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            previewBitmap?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Preview",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun AddUserScreen(
    userRepo: UserRepository,
    onBack: () -> Unit
) {
    val ctx: Context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by rememberSaveable { mutableStateOf("") }
    var pin by rememberSaveable { mutableStateOf("") }

    // IMPORTANT: must match enum names
    var role by rememberSaveable { mutableStateOf("CASHIER") }   // default role

    var showError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Log.d("AddUserScreen", "Composable shown")
    }

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Add User",
                showBack = true,
                onBack = onBack,
                onSwitchAccount = {},
                onTakeProfilePhoto = {},
                onSignOut = {},
                onAvatarClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text("Create a new user", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it.filter { ch -> ch.isDigit() }.take(6) },
                label = { Text("PIN (4–6 digits)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Role selector – now matches enum names
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Role: ", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(8.dp))

                FilterChip(
                    selected = role == "CASHIER",
                    onClick = { role = "CASHIER" },
                    label = { Text("CASHIER") },
                    modifier = Modifier.padding(end = 8.dp)
                )

                FilterChip(
                    selected = role == "ADMIN",
                    onClick = { role = "ADMIN" },
                    label = { Text("ADMIN") }
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    showError = null

                    if (username.isBlank()) {
                        showError = "Username cannot be empty"
                        return@Button
                    }
                    if (pin.length < 4) {
                        showError = "PIN must be at least 4 digits"
                        return@Button
                    }

                    scope.launch {
                        try {
                            // Map String -> enum safely
                            val roleEnum = when (role) {
                                "ADMIN" -> UserRole.ADMIN
                                "CASHIER" -> UserRole.CASHIER
                                else -> UserRole.CASHIER
                            }

                            val result = userRepo.addUser(
                                username = username,
                                role = roleEnum,
                                pin = pin,
                                photoPath = null
                            )

                            if (result.isSuccess) {
                                Toast.makeText(ctx, "User added", Toast.LENGTH_SHORT).show()
                                onBack()
                            } else {
                                showError = result.exceptionOrNull()?.message
                                    ?: "Failed to add user"
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showError = "Failed to add user: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save user")
            }

            if (showError != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = showError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}







@OptIn(ExperimentalGetImage::class)
@Composable
fun AppNavigator(
    service: Sheets,
    onExitApp: () -> Unit,
    currentScreen: String,
    setCurrentScreen: (String) -> Unit,
    currentUser: FirebaseUser?,
    setCurrentUser: (AppUser?) -> Unit,
    launchAccountChooser: () -> Unit
) {
    var scannerSource by remember { mutableStateOf("Sales") }
    var savedImagePath by remember { mutableStateOf<String?>(null) }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var showQuantityDialog by remember { mutableStateOf(false) }
    var duplicateProduct: Product? by remember { mutableStateOf(null) }
    var showSimilarProductDialog by remember { mutableStateOf(false) }
    var matchedProduct: Product? by remember { mutableStateOf(null) }
    var sheetProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var goToImageCapture by remember { mutableStateOf(false) }
    var imageCaptureSessionId by remember { mutableIntStateOf(0) }
    var showSmartPrefillDialog by remember { mutableStateOf(false) }
    var prefillProduct by remember { mutableStateOf<Product?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var cartItems by remember { mutableStateOf(listOf<Product>()) }
    var totalAmount by remember { mutableDoubleStateOf(0.0) }
    var amountTendered by remember { mutableStateOf("") }
    var change by remember { mutableDoubleStateOf(0.0) }
    var quantityInput: String by remember { mutableStateOf("") }
    var cartItemsState by remember { mutableStateOf<List<Product>>(emptyList()) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    val cameraControlState = remember { mutableStateOf<CameraControl?>(null) }
    // App user in-memory (must be var, not val)
    var currentUser by remember { mutableStateOf<AppUser?>(null) }
    var currentUserRole by rememberSaveable { mutableStateOf<UserRole?>(null) }

    var showProfileDialog by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    var users by remember { mutableStateOf(listOf(
        AppUser(uid = "admin-uid", username = "Mosa", role = UserRole.ADMIN)
        // seed the super user (you)
    )) }

// Firebase login confirmation (fires exactly once after sign-in)
    val firebaseUser by UserManager.currentUser.collectAsState()
    var authToastShown by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

// Single instance of the local user repository for this navigator
    val userRepo: UserRepository = remember {
        val db = AppDb.getInstance(context)
        LocalUserRepository(db.userDao())
    }

    LaunchedEffect(firebaseUser) {
        Log.d(
            "Auth",
            "firebaseUser update -> uid=${firebaseUser?.uid}, name=${firebaseUser?.displayName}, email=${firebaseUser?.email}"
        )
        if (firebaseUser != null && !authToastShown) {
            val who = firebaseUser?.displayName ?: firebaseUser?.email ?: "Google user"
            Toast.makeText(context, "✅ Signed in as $who", Toast.LENGTH_SHORT).show()
            authToastShown = true
        }
    }


    LaunchedEffect(Unit) {
        // make sure we have at least one admin account
        userRepo.ensureAdminSeeded()
    }



    val onCameraControlReady: (CameraControl) -> Unit = { control ->
        cameraControl = control
        cameraControlState.value = control
    }



    val activity = LocalContext.current as? MainActivity

    val coroutineScope = rememberCoroutineScope()

    // DataStore for user persistence
    val userStore = remember { UserDataStore(context) }

// Observe the saved user (null if nothing saved yet)
    val savedUserState = userStore.userFlow.collectAsState(initial = null)

    LaunchedEffect(savedUserState.value) {
        val fromDisk = savedUserState.value
        if (fromDisk != null) {
            currentUser = fromDisk
            currentUserRole = fromDisk.role
            setCurrentScreen(Routes.POS_MENU)
        } else {
            setCurrentScreen(Routes.LOGIN)
        }
    }


      var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var showProductDialog by remember { mutableStateOf(false) }
    var predictedLabel by remember { mutableStateOf<String?>(null) }
    var scannerActive by remember { mutableStateOf(true) }
    //var previewView by remember { mutableStateOf<PreviewView?>(null) }


// Your existing currentUser state (you already have this):
// var currentUser by remember { mutableStateOf(AppUser(username = "Mosa", role = UserRole.ADMIN)) }

    LaunchedEffect(savedUserState.value) {
        savedUserState.value?.let { fromDisk ->
            // populate on launch if a user was saved previously
            currentUser = fromDisk
        }
    }

    // Helper to persist any time you change the user
    fun persistCurrentUser() {
        val u = currentUser ?: return
        coroutineScope.launch { userStore.saveUser(u) }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            Log.d("User", "Persisting AppUser: ${it.username} (${it.role})")
            userStore.saveUser(it)
        }
    }




    LaunchedEffect(Unit) {
        productList = loadProductListFromGoogleSheets(context).toMutableList()
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(it) // default styling for now
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // your screen content like BarcodeScannerView, dialogs, etc.
        }
    }




    // 1. A suspend function that does all the heavy work
 fun performCheckout(scope: CoroutineScope) {
        cartItems.forEach { cartItem ->
            val matchingProduct = productList.find { it.barcode == cartItem.barcode }
            if (matchingProduct != null) {
                val updatedProduct = matchingProduct.copy(
                    quantity = (matchingProduct.quantity ) - cartItem.quantity
                )
                productList = productList.map {
                    if (it.barcode == cartItem.barcode) updatedProduct else it
                }
            } else {
                // TODO: Later handle new products
            }
        }

        scope.launch {
            updateProductsInGoogleSheets(context, productList)
        }


        cartItems = emptyList()

        val zeroQuantityProducts = productList.filter { (it.quantity ) <= 0 }

        if (zeroQuantityProducts.isNotEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "⚠️ ${zeroQuantityProducts.size} product(s) now have zero stock!"
                )
            }
        }
    }

    // 2. A small Composable function that just launches the suspend function
    val handleCheckout: () -> Unit = {
        coroutineScope.launch {
            performCheckout(coroutineScope)
        }
    }



    LaunchedEffect(Unit) {
        delay(timeMillis = 4000)

        val fetchedProducts = readProductsFromGoogleSheets(service)
        sheetProducts = fetchedProducts
        productList = fetchedProducts
        Log.d("SheetSync", "✅ Loaded ${fetchedProducts.size} products from Google Sheets.")

        Log.d("SheetLoad", "🟢 Loaded ${fetchedProducts.size} products from sheet")
        fetchedProducts.forEach {
            Log.d("SheetLoad", "→ ${it.barcode} (${it.name}) with imagePath: ${it.imagePath}")
        }

    }


    if (goToImageCapture) {
        goToImageCapture = false
        imageCaptureSessionId += 1  // 🚀 force refresh
        setCurrentScreen("ImageCapture")
    }


    when (currentScreen) {

        "Refreshing" -> {
            // Simple blank box while screen refreshes
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }

        "ImageCapture" -> {
            // Show prompt before auto-capturing image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📸 Please hold the product   ront-facing the camera...",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }


            LaunchedEffect(imageCaptureSessionId) {
                delay(4000)
                setCurrentScreen("CaptureAndProcess")
            }

        }

        "CaptureAndProcess" -> {
            CameraCaptureScreen(
                showQuantityDialog = showQuantityDialog,
                showProductDialog = showProductDialog,
                showSimilarProductDialog = showSimilarProductDialog,
                onImageCaptured = { imageBitmap ->
                    Log.d("Capture", "📸 Image captured! Bitmap size: ${imageBitmap.width}x${imageBitmap.height}")

                    // Generate unique filename using barcode or timestamp
                    val filename = scannedBarcode?.replace("[^A-Za-z0-9]".toRegex(), "_") ?: "product_${System.currentTimeMillis()}"

                    // Save image to internal storage
                    val imagePath = saveImageToInternalStorage(context, imageBitmap, filename)
                    Log.d("Capture", "✅ Image saved at path: $imagePath")
                    savedImagePath = imagePath

                    // 🧠 NEW: Try to find a similar image
                    val productImagesDir = File(context.filesDir, "product_images")
                    val existingImages = productImagesDir.listFiles { file -> file.extension == "jpg" } ?: emptyArray()

                   // var matchedProduct: Product? = null

                    Log.d("ImageMatch", "🟡 Starting comparison with locally saved images...")

                    // First check locally saved product images
                    for (file in existingImages) {
                        val existingBitmap = BitmapFactory.decodeFile(file.absolutePath)
                        if (areImagesSimilar(imageBitmap, existingBitmap)) {
                            matchedProduct = productList.find { it.imagePath == file.absolutePath }
                            if (matchedProduct != null) break
                        }
                    }
                    Log.d("SheetMatch", "🟡 No local match. Checking Google Sheets for barcode: $scannedBarcode")
                    // If no local match, check Google Sheet products by barcode
                    if (matchedProduct == null) {
                        matchedProduct = sheetProducts.find { it.barcode == scannedBarcode }
                        Log.d("SheetMatch", "✅ Sheet match result: ${matchedProduct?.name ?: "No match"}")
                    }

                    // ✅ ADD THIS BLOCK *RIGHT HERE* BELOW THE for-loop:
                    if (matchedProduct != null) {
                        Log.d("DialogTrigger", "🎯 Triggering SimilarProductDialog for: ${matchedProduct?.name}")
                        showSimilarProductDialog = true
                        scannerActive = false
                        return@CameraCaptureScreen // 👈 This stops processing and waits for user confirmation
                    }


                    //  label reading
                    coroutineScope.launch {
                        val text = extractTextFromImage(imagePath ?: "")
                        predictedLabel = text ?: "No text found"
                        scannerActive = false

                        val existingProduct = productList.find { it.barcode == scannedBarcode }
                        Log.d("BarcodeCheck", "Checking barcode: $scannedBarcode against list...")
                        if (existingProduct != null) {
                            // ✅ Only show QuantityDialog if no smart match is already detected
                            if (matchedProduct == null && prefillProduct == null) {
                                Log.d("DuplicateCheck", "🟡 Duplicate barcode detected: $scannedBarcode")
                                duplicateProduct = existingProduct
                                showQuantityDialog = true
                            } else {
                                Log.d("DuplicateCheck", "⚠️ Skipping QuantityDialog due to matched/prefilled product logic")
                            }
                            return@launch
                        }


                        showProductDialog = true
                        setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                    }


                }
,
                onBack = {
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }
            )
        }


        "Greeting" -> GreetingScreen()

        "MainMenu" -> {
            MainMenuScreen(
                onGoogleSignIn = {
                    if (UserManager.currentUser.value == null) {
                        Log.d("Auth", "GoogleSignIn requested from menu")
                        activity?.initializeGoogleSignIn()
                    } else {
                        Toast.makeText(context, "Already signed in", Toast.LENGTH_SHORT).show()
                    }
                },
                coroutineScope = coroutineScope,
                onExitApp = onExitApp,

                // NEW: wire these to your screen
                onOpenSettings = { setCurrentScreen("Settings") },
                onOpenProfile  = { setCurrentScreen("Profile") },

                // pass the nullable state
                user = currentUser,
                userRole = currentUserRole,

                // top-right avatar chip -> Profile
                onAvatarClick = { setCurrentScreen(Routes.SETTINGS) }
            )
        }


        "PosMenu" -> PosMenuScreen(
            onSalesAndTransactions = {
                if (activity?.hasCameraPermission(context) == true) {

                    cartItems = mutableListOf()
                    totalAmount = 0.0
                    amountTendered = ""
                    change = 0.00

                    setCurrentScreen("Sales")
                } else {
                    activity?.requestCameraPermission()
                }
            },
            onInventoryManagement = {
                if (activity?.hasCameraPermission(context) == true) {
                    scannerSource = "Inventory"
                    setCurrentScreen("InventoryScanner")
                } else {
                    activity?.requestCameraPermission()
                }
            },
            onViewSales = {
                setCurrentScreen("ViewSales")
            },

            // 👇 NEW LINES — these wire your buttons
            onOpenProfile  = { setCurrentScreen("Profile") },
            onOpenSettings = { setCurrentScreen("Settings") },
            userRole = currentUserRole,
            user                    = currentUser,             // 👈 NEW
            onAvatarClick           = { setCurrentScreen("Profile") }, // 👈 NEW
            onExitApp = onExitApp
        )


        "Scanner" -> {
            activity?.checkAndRequestCameraPermission()
            BarcodeScannerView(
                scannedItems = productList.map { ProductItem(it.barcode) },
                onBarcodeScanned = { barcode: String ->
                    Log.d("SalesScreen", "Scanned barcode: $barcode")

                    val existingProduct = productList.find { it.barcode == barcode }
                    if (existingProduct != null) {
                        // Increase quantity if already in cart
                        productList = productList.map {
                            if (it.barcode == barcode) {
                                it.copy(quantity = it.quantity + 1)
                            } else {
                                it
                            }
                        }
                    } else {
                        Log.d("SalesScreen", "Product not found for barcode: $barcode")

                        // For now, create a temporary product manually
                        val newProduct = Product(
                            barcode = barcode,
                            name = "New Item",
                            price = "0.0",
                            quantity = 1,
                            imagePath = null // No image yet
                        )
                        productList = productList + newProduct

                        totalAmount = productList.sumOf { it.price.toDoubleOrNull()?.times(it.quantity) ?: 0.0 }
                    }

                    // Always recalculate total after scan
                    totalAmount = productList.sumOf { it.price.toDoubleOrNull()?.times(it.quantity) ?: 0.0 }
                },
                onDuplicateScanned = { barcode ->
                    Toast.makeText(context, "📦 Already scanned: $barcode", Toast.LENGTH_SHORT).show()
                },
                onBack = {
                    setCurrentScreen("PosMenu")
                },
                productList = productList,
                setDuplicateProduct = { duplicateProduct = it },
                setShowQuantityDialog = { showQuantityDialog = it },
                setScannerActive = { scannerActive = it },
                setMatchedProduct = { matchedProduct = it },
                setShowSimilarProductDialog = { showSimilarProductDialog = it },
                scannerActive = scannerActive, // ✅ New line to pass value
                showQuantityDialog = showQuantityDialog,
                showSimilarProductDialog = showSimilarProductDialog,
                showSmartPrefillDialog = showSmartPrefillDialog,
                showProductDialog = showProductDialog,
                snackbarHostState = snackbarHostState,  // 👈 Add this line
                coroutineScope = coroutineScope,        // 👈 And this line too
                cameraControl = cameraControlState.value,
                onCameraControlReady = onCameraControlReady
                )

            // Optional: show product list below the scanner for reference
            if (scannerSource == "Sales") {
                ScannedProductList(productList)
            }
        }

        "Sales" -> {
            Scaffold(
                topBar = {
                    TuckshopTopBar(
                        title = "Sales Screen",
                        showBack = true,
                        onBack = { setCurrentScreen("PosMenu") },
                        onSwitchAccount = { launchAccountChooser() },
                        onTakeProfilePhoto = { setCurrentScreen(Routes.CAPTURE_SELFIE) }
                        ,
                        onSignOut = {
                            UserManager.signOut()
                            // Optionally re-run chooser immediately:
                            // launchAccountChooser()
                        }

                    )
                }


            ) { innerPadding ->
                SalesScreen(
                    scope = coroutineScope,
                    innerPadding = innerPadding,
                    cartItems = cartItemsState,
                    onCartItemsChange = {
                        Log.d("🛒CartFlow", "🧾 onCartItemsChange called with ${it.size} items")
                        cartItemsState = it.toList()
                    },
                    productList = productList,
                    totalAmount = totalAmount,
                    amountTendered = amountTendered,
                    change = change,
                    onQuantityChange = { product, newQuantity ->
                        val updatedList = cartItemsState.map { item ->
                            if (item.barcode == product.barcode) {
                                item.copy(quantity = newQuantity)
                            } else item
                        }.filter { it.quantity > 0 }

                        cartItemsState = updatedList
                        cartItems = updatedList

                        // ✅ Recalculate total
                        totalAmount = updatedList.sumOf { it.price.toDoubleOrNull()?.times(it.quantity) ?: 0.0 }

                        Log.d("QuantityUpdate", if (newQuantity > 0)
                            "🛠️ Updated ${product.name} to quantity $newQuantity"
                        else
                            "🗑️ Removed ${product.name} from cart (quantity was 0)"
                        )

                        Log.d("TotalCalc", "➡️ Total amount from AppNavigator: $totalAmount")

                      },
                    onAmountChange = { newAmount ->
                        amountTendered = newAmount
                        try {
                            val tendered = newAmount.toDoubleOrNull() ?: 0.0
                            change = tendered - totalAmount
                        } catch (e: Exception) {
                            change = 0.00
                        }
                    },
                    onClear = {
                        cartItems = emptyList()
                        totalAmount = 0.0
                        amountTendered = ""
                        change = 0.00
                    },
                    cameraControl = cameraControlState.value,
                    onCameraControlReady = { control: CameraControl ->
                        Log.d("FLASH", "📥 Received camera control in SalesScreen")
                        cameraControlState.value = control
                        Log.d("FLASH", "📦 Passing cameraControl to SalesScreen: ${cameraControlState.value != null}")
                    },
                            setCurrentScreen = setCurrentScreen,
                    scannerSource = "Sales"


                   )

                LaunchedEffect(cameraControlState.value) {
                    delay(2000)
                    Log.d("FLASH", "🕒 Final cameraControl after 2s: ${cameraControl != null}")
                    Log.d("FLASH", "🕒 Final cameraControlstate after 2s: ${cameraControlState.value != null}")
                }


            }
            if (showProfileDialog) {
                ProfilePhotoDialog(
                    onDismiss = { showProfileDialog = false },
                    onBitmapReady = { bmp ->
                        val uidForSave = UserManager.currentUser.value?.uid
                            ?: "Local:${currentUser?.username ?: "unknown"}"

                        coroutineScope.launch {
                            val ok = UserManager.updatePhoto(ctx, uidForSave, bmp)
                            if (ok) {
                                // read the saved path and refresh UI + Room
                                val path = ProfileStore.getPhotoPath(ctx, uidForSave)
                                if (!path.isNullOrBlank()) {
                                    currentUser = currentUser?.copy(photoPath = path)
                                    currentUser?.let { userRepo.updatePhoto(it.username, path) }
                                }
                                Toast.makeText(ctx, "Profile photo updated", Toast.LENGTH_SHORT).show()
                                setCurrentScreen(Routes.SETTINGS)
                            } else {
                                Toast.makeText(ctx, "Couldn't update profile photo", Toast.LENGTH_SHORT).show()
                            }
                            showProfileDialog = false
                        }
                    }
                )
            }

        }



        "ViewSales" -> {
            Scaffold(
                topBar = {
                    TuckshopTopBar(
                        title = "View Sales",
                        showBack = true,
                        onBack = { setCurrentScreen("PosMenu") },
                        onAvatarClick = { setCurrentScreen(Routes.SETTINGS) },
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)   // 👈 consume the Scaffold’s padding
                ) {
                    ViewSalesScreen(
                        productList = productList,
                        onBack = { setCurrentScreen("PosMenu")}
                    )
                }
            }
        }


        "InventoryScanner" -> {
            activity?.checkAndRequestCameraPermission()

            Scaffold(
                topBar = {
                    TuckshopTopBar(
                        title = "Inventory",
                        showBack = true,
                        onBack = { setCurrentScreen("PosMenu") },
                        onAvatarClick = { setCurrentScreen(Routes.SETTINGS) },

                    )
                }
            ) { innerPadding ->

                // Apply the scaffold padding to your screen content:
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    BarcodeScannerView(
                        scannedItems = productList.map { ProductItem(it.barcode) },

                        onBarcodeScanned = { barcode: String ->
                            // ✅ Play beep sound
                            try {
                                val afd = context.resources.openRawResourceFd(R.raw.beepsound)
                                val player = MediaPlayer()
                                player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                                afd.close()
                                player.prepare()
                                player.start()
                            } catch (e: Exception) {
                                Log.e("Sound", "Error playing beep sound: ${e.message}")
                            }

                            val alreadyScanned = productList.any { it.barcode == barcode }
                            if (alreadyScanned) {
                                Toast.makeText(context, "📦 Item already scanned: $barcode", Toast.LENGTH_SHORT).show()
                                duplicateProduct = productList.find { it.barcode == barcode }
                                showQuantityDialog = true
                                quantityInput = duplicateProduct?.quantity?.toString() ?: ""
                            } else {
                                scannedBarcode = barcode
                                setCurrentScreen("ImageCapture")
                            }
                        },

                        onDuplicateScanned = { barcode ->
                            Toast.makeText(context, "📦 Already scanned: $barcode", Toast.LENGTH_SHORT).show()
                        },
                        onBack = { setCurrentScreen("PosMenu") },

                        productList = productList,
                        setDuplicateProduct = { duplicateProduct = it },
                        setShowQuantityDialog = { showQuantityDialog = it },
                        setScannerActive = { scannerActive = it },
                        setMatchedProduct = { matchedProduct = it },
                        setShowSimilarProductDialog = { showSimilarProductDialog = it },

                        scannerActive = scannerActive,
                        showQuantityDialog = showQuantityDialog,
                        showSimilarProductDialog = showSimilarProductDialog,
                        showSmartPrefillDialog = showSmartPrefillDialog,
                        showProductDialog = showProductDialog,

                        snackbarHostState = snackbarHostState,
                        coroutineScope = coroutineScope,

                        cameraControl = cameraControlState.value,
                        onCameraControlReady = onCameraControlReady
                    )
                }
            }
        }




        "Settings" -> SettingsScreen(
            onBack = { setCurrentScreen("PosMenu") },

            onAddUser = {
                // next step we'll open an "AddUserDialog"
                setCurrentScreen( "AddUser")   // temporary navigation placeholder
            },
            onManageUsers = {
                setCurrentScreen("ManageUsers")
            },
            onChangePin = {
                setCurrentScreen("ChangePin")
            },
            onCaptureProfilePhoto = {
                setCurrentScreen("CaptureSelfie")
            },
            onLogout = {
                Firebase.auth.signOut()               // Google/Firebase out
                setCurrentUser(null)                  // App user out
                currentUserRole = null
                coroutineScope.launch { userStore.clearUser() }
                setCurrentScreen(Routes.LOGIN)
            },
            user = currentUser      // 👈 pass user
        )

        "Profile" -> ProfileScreen(
            onBack = { setCurrentScreen(Routes.POS_MENU) },
            user = currentUser,
                        onSwitchUser = {
                currentUser = null
                currentUserRole = null
                coroutineScope.launch { userStore.clearUser() }
                            setCurrentScreen(Routes.LOGIN)
            },
            onAvatarClick = { setCurrentScreen(Routes.SETTINGS) },
            onTakeProfilePhoto = { setCurrentScreen(Routes.CAPTURE_SELFIE) }
        )



       // "AddUser"      -> PlaceholderScreen("Add User",      onBack = { setCurrentScreen( "Settings") })
        "ChangePin"    -> PlaceholderScreen("Change My PIN", onBack = { setCurrentScreen( "Settings") })

// MainActivity.kt  (inside AppNavigator when route == Routes.CAPTURE_SELFIE)
// -----------------------------------------
// AppNavigator()  ->  case Routes.CAPTURE_SELFIE
// -----------------------------------------
        Routes.CAPTURE_SELFIE -> {
            val ctx = LocalContext.current
            var showProfileDialog by rememberSaveable { mutableStateOf(true) } // auto-open when entering

            Scaffold(
                topBar = {
                    TuckshopTopBar(
                        title = "Capture Profile Photo",
                        showBack = true,
                        onBack = { setCurrentScreen(Routes.SETTINGS) },
                        onTakeProfilePhoto = { showProfileDialog = true },
                        onSignOut = {},
                        onSwitchAccount = {},
                        onAvatarClick = {}
                    )
                }
            ) { innerPadding ->
                // Blank body – we only need the dialog
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            if (showProfileDialog) {
                ProfilePhotoDialog(
                    onDismiss = { showProfileDialog = false },
                    onBitmapReady = { bmp ->
                        val uidForSave: String =
                            UserManager.currentUser.value?.uid
                                ?: "Local:${currentUser?.username ?: "unknown"}"

                        coroutineScope.launch {
                            val ok: Boolean = UserManager.updatePhoto(ctx, uidForSave, bmp)
                            if (ok) {
                                // Read the just-saved path from DataStore and refresh UI + Room
                                val path: String? = ProfileStore.getPhotoPath(ctx, uidForSave)
                                if (path != null) {
                                    // 1) update in-memory so chip/topbar refreshes
                                    currentUser = currentUser?.copy(photoPath = path)
                                    // 2) persist in Room
                                    currentUser?.let { userRepo.updatePhoto(it.username, path) }
                                }

                                Toast.makeText(ctx, "Profile photo updated", Toast.LENGTH_SHORT).show()
                                // navigate back to Settings
                                setCurrentScreen(Routes.SETTINGS)
                            } else {
                                Toast.makeText(ctx, "Couldn't update profile photo", Toast.LENGTH_SHORT).show()
                            }

                            showProfileDialog = false
                        }
                    }
                )
            }
        }









        Routes.MANAGE_USERS -> {
            if (currentUserRole == UserRole.ADMIN) {
                    ManageUsersScreen(
                    userRepo = userRepo,
                    onBack = { setCurrentScreen(Routes.SETTINGS) },
                    onAvatarClick = { setCurrentScreen(Routes.SETTINGS) }
                )
            } else {
                Toast.makeText(context, "Admins only", Toast.LENGTH_SHORT).show()
                setCurrentScreen(Routes.SETTINGS)
            }
        }


        Routes.LOGIN -> {
            val activity: MainActivity? = LocalContext.current as? MainActivity
            val ctx: Context = LocalContext.current
            val googleSignInStarted = rememberSaveable { mutableStateOf(false) }

            LoginScreen(
                userRepo = userRepo,
                onLoggedIn = { u: AppUser ->
                    setCurrentUser(u)

                    // 2) persist top bar + profile screen
                    coroutineScope.launch {
                        ProfileStore.saveActive(
                            ctx = ctx,
                            name = u.username,
                            role = u.role.name,
                            photoPath = u.photoPath
                        )
                    }

                    // 3) (Optional) start Google sign-in once if Firebase user is null
                    if (UserManager.currentUser.value == null && !googleSignInStarted.value) {
                        googleSignInStarted.value = true
                        Log.d("Auth", "Triggering Google sign-in after App login")
                        activity?.initializeGoogleSignIn()
                    }

                    // 4) go to POS menu
                    setCurrentScreen(Routes.POS_MENU)
                },
                onBack = {
                    // From Login screen: exit the app
                    activity?.finish()
                }
            )
        }



// SETTINGS route
        Routes.SETTINGS -> {
            SettingsScreen(
                onBack = { setCurrentScreen(Routes.POS_MENU) },
                onAddUser = { setCurrentScreen(Routes.ADD_USER) },
                onManageUsers = { setCurrentScreen(Routes.MANAGE_USERS) },
                onChangePin = { /* later */ },
                onCaptureProfilePhoto = { setCurrentScreen(Routes.CAPTURE_SELFIE) },
                onLogout = {
                    Firebase.auth.signOut()               // Google/Firebase out
                    setCurrentUser(null)                  // App user out
                    currentUserRole = null
                    coroutineScope.launch { userStore.clearUser() }
                    setCurrentScreen(Routes.LOGIN)
                }
                ,
                user = currentUser,
                onAvatarClick = { setCurrentScreen(Routes.SETTINGS) }
            )
        }


// PROFILE route
        Routes.PROFILE -> {
            ProfileScreen(
                onBack = { setCurrentScreen(Routes.POS_MENU) },
                user = currentUser,                               // ← nullable is fine
                onSwitchUser = {
                    currentUser = null
                    currentUserRole = null
                    coroutineScope.launch { userStore.clearUser() }
                    setCurrentScreen(Routes.LOGIN)
                },
                onAvatarClick = { setCurrentScreen( Routes.SETTINGS) },
                onTakeProfilePhoto = { setCurrentScreen(Routes.CAPTURE_SELFIE) }
            )
        }

        Routes.ADD_USER -> {
            AddUserScreen(
                userRepo = userRepo,
                onBack = { setCurrentScreen(Routes.SETTINGS) }
            )
        }


    }

    // 🧾 Product Entry Dialog
    if (showProductDialog && scannedBarcode != null) {
        ProductEntryDialog(
            barcode = scannedBarcode!!,
            predictedLabel = predictedLabel,
            imagePath = savedImagePath,
            onSubmit = { productName, productPrice, quantity ->


                Log.d("GoogleSheets", "✅ Sheets service retrieved. Attempting write...")

                val calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Johannesburg"))
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en", "ZA"))
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "ZA"))

                dateFormat.timeZone = TimeZone.getTimeZone("Africa/Johannesburg")
                timeFormat.timeZone = TimeZone.getTimeZone("Africa/Johannesburg")

                val currentDate: String = dateFormat.format(calendar.time)
                val currentTime: String = timeFormat.format(calendar.time)

                productList = productList + Product(
                    barcode = scannedBarcode!!,
                    name = productName,
                    price = productPrice,
                    imagePath = savedImagePath,
                    quantity = quantity
                )

                writeToGoogleSheets(
                    barcode = scannedBarcode!!,
                    name = productName,
                    price = productPrice,
                    quantity = quantity,
                    date = currentDate,
                    time = currentTime,
                    service = service,
                    coroutineScope = coroutineScope,
                    imagePath = savedImagePath
                )

                // 🧹 Reset UI state after submission
                predictedLabel = null
                scannedBarcode = null
                showProductDialog = false
                setCurrentScreen("PosMenu")
                scannerActive = true
                setCurrentScreen("Refreshing")
                coroutineScope.launch {
                    delay(100)
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }

            },
            coroutineScope = coroutineScope,
            service = service,
            onDismiss = {
                Log.w("ProductEntryDialog", "🛑 Product dialog dismissed without saving")
                scannedBarcode = null
                showProductDialog = false
                predictedLabel = null
                scannerActive = true
                // 👇 Force a refresh of the scanner
                setCurrentScreen("Refreshing")
                coroutineScope.launch {
                    delay(100) // short delay before going back to scanner
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }
            }
        )
    }

    if (showQuantityDialog && scannedBarcode != null) {
        QuantityInputDialog(
            product = duplicateProduct!!,
            onSubmit = { quantity ->
                val updatedList = productList.toMutableList()
                val index = updatedList.indexOfFirst { it.barcode == scannedBarcode }

                if (index != -1) {
                    val existing = updatedList[index]
                    updatedList[index] = existing.copy(quantity = existing.quantity + quantity)
                    productList = updatedList
                    Log.d("QuantityUpdate", "✅ Quantity updated to: ${updatedList[index].quantity}")

                    // ✅ WRITE TO GOOGLE SHEETS
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Johannesburg"))
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en", "ZA"))
                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "ZA"))
                    dateFormat.timeZone = TimeZone.getTimeZone("Africa/Johannesburg")
                    timeFormat.timeZone = TimeZone.getTimeZone("Africa/Johannesburg")
                    val currentDate = dateFormat.format(calendar.time)
                    val currentTime = timeFormat.format(calendar.time)


                    writeToGoogleSheets(
                        barcode = scannedBarcode!!,
                        name = existing.name,
                        price = existing.price,
                        quantity = updatedList[index].quantity,
                        date = currentDate,
                        time = currentTime,
                        service = service,
                        coroutineScope = coroutineScope,
                        imagePath = existing.imagePath
                    )
                }



                // Reset everything
                showQuantityDialog = false
                scannedBarcode = null
                predictedLabel = null
                scannerActive = true
                duplicateProduct = null
                setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
            },
            onDismiss = {
                Log.w("QuantityInputDialog", "🛑 Dialog dismissed. No changes made.")
                showQuantityDialog = false
                scannerActive = true
                scannedBarcode = null
                predictedLabel = null
                duplicateProduct = null
                setCurrentScreen("Refreshing")
                coroutineScope.launch {
                    delay(100)
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }
            }
        )
    }

    if (showSimilarProductDialog && matchedProduct != null) {
        ConfirmSimilarProductDialog(
            product = matchedProduct!!,
            onConfirm = {
                showSimilarProductDialog = false
                prefillProduct = matchedProduct
                showSmartPrefillDialog = true
                matchedProduct = null
                setCurrentScreen("Refreshing") // 👈 force screen refresh
                coroutineScope.launch {
                    delay(100)
                    setCurrentScreen("ImageCapture")
                }
            },
            onDismiss = {
                showSimilarProductDialog = false
                matchedProduct = null
                setCurrentScreen("Refreshing")
                goToImageCapture = true
                scannerActive = true
                coroutineScope.launch {
                    delay(100)
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }
            }
        )
    }

    if (showSmartPrefillDialog && prefillProduct != null) {
        SmartPrefillDialog(
            product = prefillProduct!!,
            onSubmit = { newQty ->
                val updatedList = productList.toMutableList()
                val index = updatedList.indexOfFirst { it.barcode == prefillProduct!!.barcode }
                if (index != -1) {
                    val current = updatedList[index]
                    val updatedProduct = current.copy(quantity = current.quantity + newQty)
                    updatedList[index] = updatedProduct
                    productList = updatedList

                    // ✅ Send to Google Sheets
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Johannesburg"))
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en", "ZA"))
                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("en", "ZA"))
                    val currentDate = dateFormat.format(calendar.time)
                    val currentTime = timeFormat.format(calendar.time)


                    updateProductInGoogleSheets(
                        updatedProduct = updatedProduct,
                        date = currentDate,
                        time = currentTime,
                        service = service,
                        coroutineScope = coroutineScope
                    )
                }

                // 🔁 Reset and return to scanner
                showSmartPrefillDialog = false
                prefillProduct = null
                setCurrentScreen("Refreshing")
                coroutineScope.launch {
                    delay(100)
                    scannerActive = true // ✅ 🔥 The missing key to revive scanning!
                    setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
                }
            },

            onDismiss = {
                showSmartPrefillDialog = false
                prefillProduct = null
                scannerActive = true
                setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
            }
        )
    }



}


@Composable
fun ManageUsersScreen(
    userRepo: UserRepository,
    onBack: () -> Unit,
    onAvatarClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf(listOf<UserEntity>()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Load once
    LaunchedEffect(Unit) {
        users = userRepo.list()
    }

    fun refresh() {
        scope.launch { users = userRepo.list() }
    }

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Manage Users",
                showBack = true,
                onBack = onBack,
                onAvatarClick = onAvatarClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            if (users.isEmpty()) {
                Text(
                    "No users yet. Tap + to add one.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users, key = { it.username }) { u ->
                        UserRow(
                            user = u,
                            onToggleRole = {
                                scope.launch {
                                    val newRole =
                                        if (u.role == UserRole.ADMIN.name)
                                            UserRole.CASHIER
                                        else
                                            UserRole.ADMIN
                                    userRepo.changeRole(u.username, newRole)
                                    Toast.makeText(context, "Role updated", Toast.LENGTH_SHORT).show()
                                    refresh()
                                }
                            },
                            onResetPin = { newPin ->
                                scope.launch {
                                    userRepo.resetPin(u.username, newPin)
                                    Toast.makeText(context, "PIN reset", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    userRepo.delete(u.username)
                                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                                    refresh()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun UserRow(
    user: UserEntity,
    onToggleRole: () -> Unit,
    onResetPin: (newPin: String) -> Unit,
    onDelete: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var newPin by remember { mutableStateOf("") }

    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(user.username, style = typography.titleMedium)
                Text(user.role, style = typography.bodyMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onToggleRole) { Text("Toggle Role") }
                TextButton(onClick = { showResetDialog = true }) { Text("Reset PIN") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val ok = newPin.length in 4..8 && newPin.all { it.isDigit() }
                        if (ok) { onResetPin(newPin); showResetDialog = false }
                    }
                ) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("Cancel") } },
            title = { Text("Reset PIN") },
            text = {
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 8) newPin = it.filter(Char::isDigit) },
                    label = { Text("New PIN (4–8 digits)") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }
        )
    }
}


@Composable
fun AddUserDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (username: String, pin: String, role: UserRole) -> Unit
) {
    if (!show) return

    var username by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.CASHIER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val valid = username.isNotBlank() && pin.length in 4..8 && pin.all { it.isDigit() }
                    if (valid) onSubmit(username.trim(), pin, role)
                }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add User") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 8) pin = it.filter(Char::isDigit) },
                    label = { Text("PIN (4–8 digits)") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Role: ")
                    Spacer(Modifier.width(8.dp))
                    DropdownMenuBox(
                        current = role.name,
                        options = listOf(UserRole.CASHIER.name, UserRole.ADMIN.name),
                        onSelect = { sel -> role = UserRole.valueOf(sel) }
                    )
                }
            }
        }
    )
}

/** Tiny helper for a simple dropdown; reuse if you already have one */
@Composable
private fun DropdownMenuBox(
    current: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) { Text(current) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = { expanded = false; onSelect(opt) }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    userRepo: UserRepository,
    onLoggedIn: (AppUser) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        // make sure an admin exists the very first time the app runs
        userRepo.ensureAdminSeeded()
    }

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Login",
                showBack = true,
                onBack = onBack,
                onAvatarClick = { /* maybe open a profile/settings sheet later */ },

            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("PIN") },
                singleLine = true,
                enabled = !busy,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation()
            )

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        error = null
                        busy = true
                        val u = userRepo.verify(username.trim(), pin.trim())
                        busy = false
                        if (u != null) {
                            Toast.makeText(context, "Welcome ${u.username}", Toast.LENGTH_SHORT).show()
                            onLoggedIn(u)
                        } else {
                            error = "Invalid username or PIN"
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = !busy && username.isNotBlank() && pin.length >= 4,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (busy) "Signing in…" else "Sign in")
            }
        }
    }
}


@Composable
fun ViewSalesScreen(
    productList: List<Product>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = productList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.barcode.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2E2E))

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ListAlt,
                contentDescription = "Sales Products",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "View Sales Products",
                color = Color.White,
                style = typography.titleLarge
            )
        }


        // 🔍 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("🔍 Search by name or barcode", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(filteredList) { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color(0xFF424242), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 📷 Image
                    Image(
                        painter = rememberAsyncImagePainter(model = product.imagePath),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 📝 Product info
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = typography.bodyLarge.copy(fontSize = 18.sp),
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color(0xFF555555))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 🔲 Barcode, 💰 Price, and Quantity
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.barcode_icon),
                                contentDescription = "Barcode",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = product.barcode,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.money_icon),
                                contentDescription = "Price",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "R${product.price}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = "Quantity",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Qty: ${product.quantity}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // 🧭 Back Button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text("← Back to Menu")
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Color.Black).padding(16.dp)
    ) {
        Button(onClick = onBack) { Text("← Back") }
        Spacer(Modifier.height(12.dp))
        Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Coming next…", color = Color.White)
    }
}


@Composable
fun ScannedProductList(products: List<Product>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(8.dp)
    ) {
        Text(
            "🧾 Scanned Items:",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        products.forEach { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color.White.copy(alpha = 0.1f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!product.imagePath.isNullOrEmpty()) {
                    val file = File(product.imagePath)
                    if (file.exists()) {
                        val bitmap: Bitmap? = BitmapFactory.decodeFile(product.imagePath)
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                    } else {
                        Log.w("ProductImage", "⚠️ Image file does not exist: ${product.imagePath}")
                    }
                }

                Column {
                    Text("📦 ${product.name}", color = Color.White)
                    Text("R ${product.price} x ${product.quantity}", color = Color.LightGray)
                }
            }
        }
    }
}


@Composable
fun SmartPrefillDialog(
    product: Product,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var inputQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Same Product - Add Quantity") },
        text = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // LEFT: Image
                    // LEFT: Image
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        val imagePath = product.imagePath
                        val fileExists = remember(imagePath) { imagePath != null && File(imagePath).exists() }

                        if (fileExists) {
                            Log.d("ImageLoad","✅ SmartPrefillDialog image exists at path: $imagePath")
                            val bitmap: Bitmap? = remember(imagePath) {
                                BitmapFactory.decodeFile(imagePath!!)
                            }

                            bitmap?.let {
                                Log.d("ImageLoad","✅ SmartPrefillDialog loaded image successfully: $imagePath")
                                FadeInImage(
                                    bitmap = it,
                                    modifier = Modifier.fillMaxWidth().height(150.dp)
                                )

                            } ?: run {
                                Log.w("ImageLoad","❌ SmartPrefillDialog failed to decode image at path: $imagePath")
                                Image(
                                    painter = painterResource(id = R.drawable.no_image_placeholder),
                                    contentDescription = "No Image Available",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            }
                        } else {
                            Log.w("ImageLoad","❌ SmartPrefillDialog file does not exist or imagePath is null: $imagePath")
                            Image(
                                painter = painterResource(id = R.drawable.no_image_placeholder),
                                contentDescription = "No Image Available",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                    }


                    // RIGHT: Info
                    Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(text = "Name: ${product.name}")
                        Text(text = "Barcode: ${product.barcode}")
                        Text(text = "Price: R${product.price}")
                        Text(text = "Current Qty: ${product.quantity}")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputQuantity,
                            onValueChange = { inputQuantity = it },
                            label = { Text("New Quantity to Add") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val qty = inputQuantity.toIntOrNull() ?: 0
                onSubmit(qty)

                // 🧹 Reset states and return to scanner
                inputQuantity = ""
                onDismiss() // this will call the dismiss logic you passed from AppNavigator
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun QuantityInputDialog(
    product: Product,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
)
{
    var inputQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Quantity") },
        text = {
            DialogCard {
                Row(modifier = Modifier.fillMaxWidth()) {

                    // LEFT: Image
                    val imagePath = product.imagePath
                    val fileExists = remember(imagePath) {
                        imagePath != null && File(imagePath).exists()
                    }

                    Column(modifier = Modifier.weight(1f).padding(end = 18.dp)) {
                        if (fileExists) {
                            Log.d("ImageLoad", "✅ QuantityDialog image exists at path: $imagePath")
                            val bitmap: Bitmap? = remember(imagePath) {
                                BitmapFactory.decodeFile(imagePath!!)
                            }

                            bitmap?.let {
                                Log.d(
                                    "ImageLoad",
                                    "✅ QuantityDialog loaded image successfully: $imagePath"
                                )
                                FadeInImage(
                                    bitmap = it,
                                    modifier = Modifier.fillMaxWidth().height(150.dp)
                                )

                            } ?: run {
                                Log.w(
                                    "ImageLoad",
                                    "❌ QuantityDialog failed to decode image at path: $imagePath"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.no_image_placeholder),
                                    contentDescription = "No Image Available",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            }
                        } else {
                            Log.w(
                                "ImageLoad",
                                "❌ QuantityDialog file does not exist or imagePath is null"
                            )
                            Image(
                                painter = painterResource(id = R.drawable.no_image_placeholder),
                                contentDescription = "No Image Available",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                    }

                    // RIGHT: Product Info + Quantity
                    Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("Barcode: ${product.barcode}")
                        Text("Price: R${product.price}")
                        Text("Current Qty: ${product.quantity}")
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inputQuantity,
                            onValueChange = { inputQuantity = it },
                            label = { Text("New Quantity to Add") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val quantity = inputQuantity.toIntOrNull() ?: 1
                onSubmit(quantity)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun ConfirmSimilarProductDialog(
    product: Product,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Same Item?") },
        text = {
            DialogCard {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // 🖼️ LEFT: Product Image
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        val imagePath = product.imagePath
                        val fileExists = remember(imagePath) {
                            imagePath != null && File(imagePath).exists()
                        }

                        if (fileExists) {
                            val bitmap: Bitmap? = remember(imagePath) {
                                BitmapFactory.decodeFile(imagePath!!)
                            }

                            bitmap?.let {
                                Log.d(
                                    "ImageLoad",
                                    "✅ ConfirmSimilarProductDialog image loaded successfully from $imagePath"
                                )
                                FadeInImage(
                                    bitmap = it,
                                    modifier = Modifier.fillMaxWidth().height(150.dp)
                                )
                            } ?: run {
                                Log.w(
                                    "ImageLoad",
                                    "❌ ConfirmSimilarProductDialog failed to decode image at path: $imagePath"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.no_image_placeholder),
                                    contentDescription = "No Image Available",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            }
                        } else {
                            Log.w(
                                "ImageLoad",
                                "❌ ConfirmSimilarProductDialog file does not exist or imagePath is null"
                            )
                            Image(
                                painter = painterResource(id = R.drawable.no_image_placeholder),
                                contentDescription = "No Image Available",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                    }

                    // 📋 RIGHT: Product Info
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🧾 Name: ${product.name}")
                        Text("🔢 Barcode: ${product.barcode}")
                        Text("ZAR Price: R${product.price}")
                        Text("📦 Quantity: ${product.quantity}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Is this the same item?", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )

}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    user: AppUser?, // This should already be from UserManager
    onSwitchUser: () -> Unit,
    onAvatarClick: () -> Unit,
    onTakeProfilePhoto: () -> Unit
) {
    val ctx = LocalContext.current

    val savedName by ProfileStore.nameFlow(ctx).collectAsState(initial = null)
    val savedRole by ProfileStore.roleFlow(ctx).collectAsState(initial = null)
    var photoToShow by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user?.uid) {
        if (user?.uid != null) {
            photoToShow = ProfileStore.getPhotoPath(ctx, user.uid)
        }
    }



    val name = savedName ?: user?.username ?: "—"
    val role = savedRole ?: user?.role?.name ?: "—"

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Profile",
                showBack = true,
                onBack = onBack,
                onTakeProfilePhoto = onTakeProfilePhoto,
                onSwitchAccount = onSwitchUser,
                onSignOut = {},
                onAvatarClick = onAvatarClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!photoToShow.isNullOrBlank()) {
                val model = photoToShow
                Image(
                    painter = rememberAsyncImagePainter(model),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(text = "Username: $name")
            Text(text = "Role: $role")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onTakeProfilePhoto) {
                Text("Update profile photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            var showConfirm by remember { mutableStateOf(false) }
            Button(onClick = { showConfirm = true }) {
                Text("Switch user")
            }
            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    title = { Text("Switch user?") },
                    text = { Text("You'll be logged out and returned to the login screen.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showConfirm = false
                            onSwitchUser()
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirm = false }) {
                            Text("No")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            var showLogout by remember { mutableStateOf(false) }
            Button(onClick = { showLogout = true }) {
                Text("Log out")
            }
            if (showLogout) {
                AlertDialog(
                    onDismissRequest = { showLogout = false },
                    title = { Text("Log out?") },
                    text = { Text("You will be signed out and your profile data will be cleared.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogout = false
                            // Clear stored profile info
                            CoroutineScope(Dispatchers.IO).launch {
                                ProfileStore.saveActive(ctx, "", "", null)
                                ProfileStore.clearPhoto(ctx)
                                withContext(Dispatchers.Main) {
                                    onSwitchUser() // Or replace with onLogout() if you add that
                                }
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogout = false }) {
                            Text("No")
                        }
                    }
                )
            }


        }
    }
}



@Composable
fun UserAvatarChip(
    user: AppUser,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val ctx = LocalContext.current

    var avatarPath by remember { mutableStateOf<String?>(user.photoPath) }
    LaunchedEffect(user.uid) {
        avatarPath = ProfileStore.getPhotoPath(ctx, user.uid) ?: user.photoPath
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF263238))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle photo or initials fallback
        val photoPath = avatarPath
        if (photoPath != null && File(photoPath).exists()) {
            val bmp = remember(photoPath) { BitmapFactory.decodeFile(photoPath) }
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "User photo",
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF455A64)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (user.username.firstOrNull() ?: '?').uppercaseChar().toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(8.dp))
        Text(user.username, color = Color.White, fontSize = 14.sp)
    }
}





@Composable
fun PosMenuScreen(
    onSalesAndTransactions: () -> Unit,
    onInventoryManagement: () -> Unit,
    onViewSales: () -> Unit,
    onOpenProfile: () -> Unit,     // already there
    onOpenSettings: () -> Unit,    // already there
    userRole: UserRole?,           // <-- make nullable
    user: AppUser?,                // <-- make nullable
    onAvatarClick: (() -> Unit)?,  // <-- already nullable in your file; keep it
    onExitApp: () -> Unit
) {

// Header row with title (left) + avatar (right)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Tuckshop POS",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.weight(1f))

        // 👇 Clickable avatar – when tapped, go to Profile
// Header row avatar chip (only show if we have a user AND a click handler)
        UserAvatarChip(
            user = user ?: return@Row,          // early skip if null, or:
            onClick = onAvatarClick ?: {}
        )
// If you prefer not to early return, you can wrap it:
        if (user != null && onAvatarClick != null) {
            UserAvatarChip(user = user, onClick = onAvatarClick)
        }

    }

    Spacer(Modifier.height(8.dp))


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tuckshop POS",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = onSalesAndTransactions, // 👈 This is the button that triggers scanning
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Sales and Transactions", fontFamily = FontFamily.SansSerif)
            }

            Button(
                onClick = onInventoryManagement,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Inventory Management", fontFamily = FontFamily.SansSerif)
            }

            Button(
                onClick = onViewSales,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("View Sales", fontFamily = FontFamily.SansSerif)
            }

            Button(
                onClick = onOpenProfile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Profile", fontFamily = FontFamily.SansSerif)
            }

// Role-based Settings / My Account button
            Button(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                val buttonLabel = if (userRole == UserRole.ADMIN) "Settings" else "My Account"
                Text(text = buttonLabel, fontFamily = FontFamily.SansSerif)
            }


            Button(
                onClick = onExitApp,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Exit", fontFamily = FontFamily.SansSerif)
            }
        }
    }
}


@Composable
fun GreetingScreen() {
    val greetings = listOf(
        "May God's grace guide our journey today.",
        "The Lord is my shepherd, I shall not want. - Psalm 23:1",
        "Faith is the key to unlock the impossible.",
        "With God, all things are possible. - Matthew 19:26",
        "Start your day with prayer, and watch blessings unfold.",
        "Seek first the kingdom of God, and all shall be added to you. - Matthew 6:33",
        "The joy of the Lord is my strength. - Nehemiah 8:10",
        "Trust in the Lord with all your heart. - Proverbs 3:5",
        "I can do all things through Christ who strengthens me. - Philippians 4:13",
        "Commit to the Lord whatever you do, and He will establish your plans. - Proverbs 16:3",
        "Be strong and courageous, for the Lord your God is with you. - Joshua 1:9",
        "For with God nothing shall be impossible. - Luke 1:37",
        "Let your light shine before others. - Matthew 5:16",
        "Whatever you do, work at it with all your heart. - Colossians 3:23",
        "Start where you are. Use what you have. Do what you can. - Arthur Ashe",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "Faith is taking the first step even when you don't see the whole staircase. - Martin Luther King Jr.",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill",
        "God has a purpose for your pain, a reason for your struggle, and a reward for your faithfulness. - Unknown",
        "The best way to predict the future is to create it. - Peter Drucker",
        "Your present circumstances don't determine where you can go; they merely determine where you start. - Nido Qubein",
        "God is within her; she will not fall. - Psalm 46:5",
        "The Lord will fight for you; you need only to be still. - Exodus 14:14",
        "Walk by faith, not by sight. - 2 Corinthians 5:7",
        "Cast all your anxiety on Him because He cares for you. - 1 Peter 5:7",
        "The steadfast love of the Lord never ceases; His mercies never come to an end. - Lamentations 3:22-23",
        "Faith over fear.",
        "Rise and shine in His glory.",
        "Blessed beyond measure.",
        "God is greater than the highs and lows.",
        "Grace upon grace. - John 1:16",
        "Walk humbly with your God. - Micah 6:8",
        "Be still and know that I am God. - Psalm 46:10",
        "Chosen, loved, redeemed.",
        "Hope anchors the soul. - Hebrews 6:19",
        "God’s promises never fail.",
        "Rooted in faith, growing in grace.",
        "Prayer changes everything.",
        "Let go and let God.",
        "Shine bright, for His glory.",
        "Love never fails. - 1 Corinthians 13:8",
        "Strength rises when we wait on the Lord. - Isaiah 40:31",
        "Create a life you can't wait to wake up to.",
        "Dream big, pray bigger.",
        "Small steps every day lead to big changes.",
        "Where God guides, He provides.",
        "Favor follows faith.",
        "Nothing is impossible with God. - Luke 1:37",
        "Overflow with hope by the power of the Holy Spirit. - Romans 15:13",
        "Move forward with faith, not fear.",
        "Start each day with a grateful heart."


    )
    val randomGreeting = greetings[Random.nextInt(greetings.size)]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = randomGreeting, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "\"Faith Gives Us Strength\"", color = Color.Yellow, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}


@OptIn(ExperimentalGetImage::class)
@Composable
fun MainMenuScreen(
    onGoogleSignIn: () -> Unit,
    coroutineScope: CoroutineScope,
    onExitApp: () -> Unit,
    // NEW: add these so AppNavigator can wire buttons
    onOpenSettings: () -> Unit,
    onOpenProfile: () -> Unit,
    // NEW: allow nullable values coming from AppNavigator
    user: AppUser?,
    userRole: UserRole?,
    // NEW: optional avatar click for the top bar chip
    onAvatarClick: (() -> Unit)? = null,
) {
    val ctx = LocalContext.current
    val avatarPath: String? = user?.photoPath   // <-- use AppUser photo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional top bar (keeps your current look & feel)
            TuckshopTopBar(
                title = "POS Menu",
                showBack = true,
                onBack = { },
                onAvatarClick = onAvatarClick ?: {},
                avatarPath = avatarPath
            )

            Button(
                onClick = onGoogleSignIn,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text(text = "Sign in with Google", fontFamily = FontFamily.SansSerif) }

            Button(
                onClick = onExitApp,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text(text = "Exit", fontFamily = FontFamily.SansSerif) }

            Button(
                onClick = onOpenSettings,              // ← wire it
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text(text = "Settings", fontFamily = FontFamily.SansSerif) }

            Button(
                onClick = onOpenProfile,               // ← wire it
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text(text = "Profile", fontFamily = FontFamily.SansSerif) }

            // Tiny “who is signed in” footer — safe calls, no NPEs
            val who  = user?.username ?: "Guest"
            val role = userRole?.name ?: "--"
            Text(text = "User: $who   |   Role: $role", color = Color.White)
        }
    }
}



@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onAddUser: () -> Unit,
    onManageUsers: () -> Unit,
    onChangePin: () -> Unit,
    onCaptureProfilePhoto: () -> Unit,
    onLogout: () -> Unit,
    user: AppUser? = null,
    onAvatarClick: (() -> Unit)? = null
) {

    val ctx = LocalContext.current
    val fbUser by UserManager.currentUser.collectAsState()
    val savedName by ProfileStore.nameFlow(ctx).collectAsState(initial = null)
    val savedRole by ProfileStore.roleFlow(ctx).collectAsState(initial = null)
    val localPhoto by ProfileStore.photoPathFlow(ctx).collectAsState(initial = null)

    val name = fbUser?.displayName ?: savedName ?: fbUser?.email?.substringBefore('@') ?: "User"
    val role = savedRole ?: "CASHIER"
    val avatar = localPhoto ?: fbUser?.photoUrl?.toString()
    val avatarPath: String? = user?.photoPath

    Scaffold(
        topBar = {
            TuckshopTopBar(
                title = "Settings",
                showBack = true,
                onBack = onBack,
                onSwitchAccount = { /* if you want */ },
                onTakeProfilePhoto = onCaptureProfilePhoto,   // 👈 important
                onSignOut = onLogout,
                onAvatarClick = onAvatarClick ?: {},
                avatarPath = avatarPath
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)        // 👈 use the scaffold padding
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // (Optional) You can remove this Back button since the top bar has one
            // Button(onClick = onBack) { Text("← Back") }

            Text("Settings", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Admin options", color = Color(0xFFB0BEC5))

            Button(
                onClick = onAddUser,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) { Text("Add User (Admin/Cashier)") }

            Button(
                onClick = onManageUsers,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) { Text("Manage Users") }

            Button(
                onClick = onChangePin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) { Text("Change My PIN") }

            Button(
                onClick = onCaptureProfilePhoto,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) { Text("Capture Profile Photo") }

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) { Text("Logout") }
        }
    }
}


@Composable
fun ProductEntryDialog(
    barcode: String,
    predictedLabel: String? = null,
    imagePath: String? = null,
    onSubmit: (String, String, Int) -> Unit,
    coroutineScope: CoroutineScope,
    service: Sheets,
    onDismiss: () -> Unit
) {
    var productPrice by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf(predictedLabel ?: "") }
    var inputQuantity by remember { mutableStateOf("") }




    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false),
                title = { Text("Enter Product Name") },
        text = {
            DialogCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ) {
                    // LEFT SIDE: Image
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (imagePath != null) {
                            val bitmap = remember(imagePath) {
                                BitmapFactory.decodeFile(imagePath)
                            }

                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Captured Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }
                        }
                    }

                    // RIGHT SIDE: Fields
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Scanned Barcode: $barcode")

                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            label = { Text("Product Name") }
                        )

                        OutlinedTextField(
                            value = productPrice,
                            onValueChange = { productPrice = it },
                            label = { Text("Price (R)") }
                        )

                        OutlinedTextField(
                            value = inputQuantity,
                            onValueChange = { inputQuantity = it },
                            label = { Text("Quantity") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        }
,
        confirmButton = {
            Button(onClick = {
                val quantity = inputQuantity.toIntOrNull() ?: 1
                val product = Product(
                    barcode = barcode,
                    name = productName,
                    price = productPrice,
                    quantity = quantity,
                    imagePath = imagePath
                )
                onSubmit(product.name, product.price, product.quantity)
                coroutineScope.launch {
                    addProductToGoogleSheets(service, product)
                }


                onDismiss()
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun oldProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }

}

fun saveImageToInternalStorage(
    context: Context,
    bitmap: Bitmap,
    filename: String
): String? {
    return try {
        val directory = File(context.filesDir, "profile_photos")
        if (!directory.exists()) directory.mkdirs()

        val file = File(directory, "$filename.jpg")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.flush()
        stream.close()

        file.absolutePath
    } catch (e: Exception) {
        Log.e("ImageSave", "Error saving image", e)
        null
    }
}


@Composable
fun CameraCaptureScreen(
    showQuantityDialog: Boolean,
    showProductDialog: Boolean,
    showSimilarProductDialog: Boolean,
    onImageCaptured: (Bitmap) -> Unit,
    onBack: () -> Unit
)
 {
    val context = LocalContext.current
    //val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    LaunchedEffect(Unit) {
        delay(4000) // ⏳ Auto-capture after 4 seconds

        // 🚨 Only continue if no dialogs are open
        if (!showQuantityDialog && !showProductDialog && !showSimilarProductDialog) {

            val cameraProvider = ProcessCameraProvider.getInstance(context).get()

            cameraProvider.unbindAll()

            val bitmap = previewView?.bitmap
            if (bitmap != null) {
                Log.d("Capture", "📸 Image captured! Bitmap size: ${bitmap.width}x${bitmap.height}")

                val image = InputImage.fromBitmap(bitmap, 0)
                val scanner = BarcodeScanning.getClient()

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            val value = barcode.rawValue
                            Log.d("BarcodeScanner", "✅ Barcode detected: $value")
                        }
                        onImageCaptured(bitmap)
                    }
                    .addOnFailureListener { e ->
                        Log.e("BarcodeScanner", "❌ Barcode scanning failed", e)
                        onImageCaptured(bitmap)
                    }
            } else {
                Log.e("Capture", "❌ Failed to capture image: bitmap is null")
                onBack()
            }
        } else {
            Log.d("Capture", "⏸️ Skipped image capture because a dialog is open")
        }
    }


    DisposableEffect(Unit) {
        val scanner = BarcodeScanning.getClient()
        onDispose {
            scanner.close() // 🧹 Clean up
            Log.d("BarcodeScanner", "🧹 Scanner closed safely")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { it ->
                PreviewView(it).also { view ->
                    previewView = view

                    val cameraProvider = ProcessCameraProvider.getInstance(it).get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(view.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            context as ComponentActivity,
                            cameraSelector,
                            preview
                        )
                    } catch (e: Exception) {
                        Log.e("Capture", "🚨 Error binding preview use case", e)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}




@ExperimentalGetImage
@Composable
fun BarcodeScannerView(
    scannedItems: List<ProductItem>,
    onBarcodeScanned: (String) -> Unit,
    onDuplicateScanned: (String) -> Unit,
    onBack: () -> Unit,
    productList: List<Product>,
    setDuplicateProduct: (Product?) -> Unit,
    setShowQuantityDialog: (Boolean) -> Unit,
    setScannerActive: (Boolean) -> Unit,
    setMatchedProduct: (Product?) -> Unit,
    setShowSimilarProductDialog: (Boolean) -> Unit,
    scannerActive: Boolean,
    showQuantityDialog: Boolean,
    showSimilarProductDialog: Boolean,
    showSmartPrefillDialog: Boolean,
    showProductDialog: Boolean,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    cameraControl: CameraControl?,
    onCameraControlReady: (CameraControl) -> Unit

    )


 {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as ComponentActivity


    // Step 1: Flashlight state toggle
    var isTorchOn by remember { mutableStateOf(false) }

// This will hold the control object to turn torch on/off
    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
     var isProcessing by remember { mutableStateOf(false) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }

     val scanner: BarcodeScanner = BarcodeScanning.getClient()


     // 🆕 Track recent barcode scans
     data class ScannedBarcodeData(val value: String, val timestamp: Long)
     val recentScans = remember { mutableStateListOf<ScannedBarcodeData>() }

     fun levenshteinDistance(s: String, t: String): Int {
         val m = s.length
         val n = t.length
         val dp = Array(m + 1) { IntArray(n + 1) }

         for (i in 0..m) dp[i][0] = i
         for (j in 0..n) dp[0][j] = j

         for (i in 1..m) {
             for (j in 1..n) {
                 val cost = if (s[i - 1] == t[j - 1]) 0 else 1
                 dp[i][j] = minOf(
                     dp[i - 1][j] + 1,
                     dp[i][j - 1] + 1,
                     dp[i - 1][j - 1] + cost
                 )
             }
         }

         return dp[m][n]
     }


     fun similarityScore(a: String, b: String): Double {
         val maxLen = maxOf(a.length, b.length)
         if (maxLen == 0) return 1.0
         val editDist = levenshteinDistance(a, b)
         return 1.0 - editDist.toDouble() / maxLen
     }


     fun getVerifiedBarcode(): String? {
         val currentTime = System.currentTimeMillis()
         val window = 2000 // 2 seconds

         val recentEntries = recentScans.filter { currentTime - it.timestamp <= window }

         for (i in recentEntries.indices) {
             val base = recentEntries[i]
             val similarCount = recentEntries.count {
                 it != base && similarityScore(it.value, base.value) >= 0.98
             }

             if (similarCount >= 2) return base.value
         }

         return null
     }


     LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

// Unbind all use cases before rebinding
        cameraProvider.unbindAll()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val preview = Preview.Builder().build()



        previewView?.let { view ->
            preview.setSurfaceProvider(view.surfaceProvider)
        }



        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it ->
                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (!isProcessing && mediaImage != null && scannerActive && !showQuantityDialog && !showSimilarProductDialog && !showProductDialog && !showSmartPrefillDialog)
                    {
                        isProcessing = true

                        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    barcode.rawValue?.let { value ->

                                        val verified = getVerifiedBarcode()

                                        if (verified != null) {
                                            // ✅ Beep only on verified barcode
                                            try {
                                                val afd = context.resources.openRawResourceFd(R.raw.beepsound)
                                                val player = MediaPlayer()
                                                player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                                                afd.close()
                                                player.prepare()
                                                player.start()
                                            } catch (e: Exception) {
                                                Log.e("Sound", "Error playing beep sound: ${e.message}")
                                            }

                                            val alreadyScanned: Boolean = scannedItems.any { it.barcode == verified }

                                            if (alreadyScanned) {
                                                val foundProduct = productList.find { it.barcode == verified }
                                                setDuplicateProduct(foundProduct)

                                                foundProduct?.let {
                                                    setMatchedProduct(foundProduct)
                                                    setShowSimilarProductDialog(true)
                                                    setScannerActive(false)
                                                }
                                            } else {
                                                onBarcodeScanned(verified)

                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("✅ New item scanned and added!")
                                                }

                                            }


                                        } else {
                                            // 🕒 Not yet verified, so store it for future
                                            recentScans.add(ScannedBarcodeData(value, System.currentTimeMillis()))
                                        }
                                    }
}
                            }
                            .addOnFailureListener { e ->
                                Log.e("BarcodeScanner", "Error: ${e.message}")
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                                isProcessing = false
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }

        cameraProvider.unbindAll()

        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
         Log.d("FLASH", "✅ Passing camera control to parent from BarcodeScannerView")

         onCameraControlReady(camera.cameraControl)
        // ✅ Store the camera control for toggling flashlight
        cameraControl = camera.cameraControl
        // val control = camera.cameraControl
         //Log.d("FLASH", "✅ Sending CameraControl to parent")
         //onCameraControlReady(control)
    }





     DisposableEffect(Unit) {
         onDispose {
             scanner.close()
             Log.d("BarcodeScanner", "🧹 Scanner closed safely")
         }
     }


     Box(modifier = Modifier.fillMaxSize()) {

         val infiniteTransition = rememberInfiniteTransition(label = "")
         val animatedAlpha by infiniteTransition.animateFloat(
             initialValue = 0.3f,
             targetValue = 1f,
             animationSpec = infiniteRepeatable(
                 animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                 repeatMode = RepeatMode.Reverse
             ), label = ""
         )

         Box(
             modifier = Modifier
                 .align(Alignment.Center)
                 .size(250.dp)
                 .border(
                     width = 4.dp,
                     color = Color.Cyan.copy(alpha = animatedAlpha),
                     shape = RoundedCornerShape(12.dp)
                 )
         )


         AndroidView(
            factory = {
                // Create a fresh new instance each time
                PreviewView(it).also { view ->
                    previewView = view
                }
            },
             modifier = Modifier.matchParentSize()
         )


        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Flashlight Toggle Button
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    isTorchOn = !isTorchOn
                    cameraControl?.enableTorch(isTorchOn)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTorchOn) Color.Yellow else Color.Gray
                )
            ) {
                Text(text = if (isTorchOn) "Flashlight ON" else "Flashlight OFF")
            }
        }
    }




}

// 📌 Place this outside any class or @Composable function
fun areImagesSimilar(bitmap1: Bitmap, bitmap2: Bitmap, tolerance: Int = 30): Boolean {
    val resized1 = Bitmap.createScaledBitmap(bitmap1, 16, 16, true)
    val resized2 = Bitmap.createScaledBitmap(bitmap2, 16, 16, true)

    var diff = 0L
    for (x in 0 until 16) {
        for (y in 0 until 16) {
            val pixel1 = resized1.getPixel(x, y)
            val pixel2 = resized2.getPixel(x, y)

            val r1 = (pixel1 shr 16) and 0xff
            val g1 = (pixel1 shr 8) and 0xff
            val b1 = pixel1 and 0xff

            val r2 = (pixel2 shr 16) and 0xff
            val g2 = (pixel2 shr 8) and 0xff
            val b2 = pixel2 and 0xff

            diff += abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2)
        }
    }

    val averageDiff = diff / (16 * 16)
    return averageDiff < tolerance
}

suspend fun readProductsFromGoogleSheets(service: Sheets): List<Product> {
    return withContext(Dispatchers.IO) {
        try {
            val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA"
            val range = "Sheet1!A2:G" // Skip header row if you have one

            val response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute()

            val values = response.getValues() ?: return@withContext emptyList()

            values.mapNotNull { row ->
                if (row.size >= 7) {
                    val barcode = row[0].toString()
                    val name = row[1].toString()
                    val price = row[2].toString()
                    val quantity = row[3].toString().toIntOrNull() ?: 1
                    val date = row[4].toString()
                    val time = row[5].toString()
                    val imagePath = if (row.size > 6) row[6].toString() else null

                    Product(barcode, name, price, imagePath, quantity)

                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleSheets", "❌ Failed to read data: ${e.message}", e)
            emptyList()
        }
    }
}

@Composable
fun DialogCard(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        content()
    }
}

fun getTodayDate(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}

fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}


@kotlin.OptIn(ExperimentalFoundationApi::class)
@Composable
fun SalesScreen(
    scope: CoroutineScope,
    innerPadding: PaddingValues,
    cartItems: List<Product>,
    onCartItemsChange: (List<Product>) -> Unit,
    productList: List<Product>,
    totalAmount: Double,
    amountTendered: String,
    change: Double,
    onQuantityChange: (Product, Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onClear: () -> Unit,
    cameraControl: CameraControl?,
    onCameraControlReady: (CameraControl) -> Unit = {},
    setCurrentScreen: (String) -> Unit,
    scannerSource: String = "Sales"
) {
    var lastUpdatedBarcode by remember { mutableStateOf<String?>(null) }
    var barcodeDetectedTime by remember { mutableLongStateOf(0L) }
    var barcodeConfirmed by remember { mutableStateOf(false) }
    var showNewProductDialog by remember { mutableStateOf(false) }
    var newScannedBarcode by remember { mutableStateOf<String?>(null) }
    var totalAmountState by remember { mutableDoubleStateOf(totalAmount) }
    var cartItemsState = remember { mutableStateListOf<Product>() }
    var amountTenderedState by remember { mutableStateOf(amountTendered) }
    var changeState by remember { mutableStateOf(0.0) }
    var localProductList by remember { mutableStateOf(productList) }
    var isProductListLoaded by remember { mutableStateOf(false) }
    var scannerActive by remember { mutableStateOf(false) }
    var scannedBarcodeToResume by remember { mutableStateOf<String?>(null) }
    var isAnyDialogOpen by remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    var capturedImagePath by remember { mutableStateOf<String?>(null) }
    var predictedLabel by remember { mutableStateOf<String?>(null) }
    var isLoadingPrediction by remember { mutableStateOf(false) }
    var shouldLaunchCamera by remember { mutableStateOf(false) }
// Place near the top of your composable or class
    var lastScannedBarcode by remember { mutableStateOf<String?>(null) }
    var lastScanTime by remember { mutableStateOf(0L) }
    val cooldownMillis = 2000L // 2-second cooldown
    var showIouDialog by remember { mutableStateOf(false) }
    var iouNameInput by remember { mutableStateOf("") }
    var pendingCheckout by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    var savedImagePath by remember { mutableStateOf<String?>(null) }
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var currentScreen by remember { mutableStateOf("Scanner") }
    var scannerSource by remember { mutableStateOf("Sales") } // or default value
    var isTorchOn by remember { mutableStateOf(false) }



    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val service = (context as? MainActivity)?.getSheetsService(context)

    fun refreshProductList() {
        coroutineScope.launch {
            localProductList = loadProductListFromGoogleSheets(context).toMutableList()
        }
    }

    val refreshedProductList: () -> Job = remember {
        {
            coroutineScope.launch {
                val fetched: MutableList<Product> =
                    loadProductListFromGoogleSheets(context).toMutableList()
                Log.d("ScannerFlow", "📥 Fetched ${fetched.size} items from Sheets")

                Log.d(
                    "ScannerFlow",
                    "📋 localProductList before update: ${localProductList.size} items"
                )
                localProductList = fetched
                Log.d(
                    "ScannerFlow",
                    "✅ localProductList after update: ${localProductList.size} items"
                )

                isProductListLoaded = true
                scannerActive = true // ✅ re-enable scanning after refresh
            }
        }
    }

    LaunchedEffect(cameraControl) {
        Log.d("FLASH", "📥 Received cameraControl in SalesScreen: ${cameraControl != null}")

    }

    LaunchedEffect(cameraControl) {
        Log.d("FLASH", "🧪 cameraControl passed into SalesScreen: ${cameraControl != null}")
    }


    LaunchedEffect(Unit) {
        Log.d(
            "ScannerFlow",
            "📥 Initial productList passed to SalesScreen has ${productList.size} items"
        )
        if (productList.isEmpty()) {
            Log.d(
                "ScannerFlow",
                "🔁 Refreshing product list from Sheets because initial list is empty"
            )
            refreshProductList()
        } else {
            Log.d("ScannerFlow", "✅ Using passed-in productList as source")
            localProductList = productList.toMutableList()
            isProductListLoaded = true
            scannerActive = true // ✅ ACTIVATE SCANNER HERE
        }
    }

    LaunchedEffect(cartItems) {
        Log.d("🛒CartInit", "✅ cartItemsState initialized with ${cartItems.size} items from parent")
        cartItemsState.clear()
        cartItemsState.addAll(cartItems)
    }

    LaunchedEffect(cartItemsState) {
        var runningTotal = 0.0
        cartItemsState.forEach {
            val price = it.price.toDoubleOrNull() ?: 0.0
            val subtotal = price * it.quantity
            Log.d(
                "TotalCalc",
                "Item: ${it.name}, Raw Price: '${it.price}', Parsed Price: $price, Qty: ${it.quantity}, Subtotal: $subtotal"
            )
            runningTotal += subtotal
        }
        totalAmountState = runningTotal
        Log.d(
            "TotalCalc",
            "➡️ Total amount from Launched Effect for Cart Items State1: $runningTotal"
        )
    }



    LaunchedEffect(amountTenderedState, totalAmountState) {
        val tendered: Double? = amountTenderedState.toDoubleOrNull()
        Log.d("ChangeDue", "💰 Tendered: $tendered | Total: $totalAmountState")
        changeState = if (tendered != null) {
            tendered - totalAmountState
        } else {
            0.0
        }
        Log.d("ChangeDue", "🧮 Change Due: $changeState")
    }


    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && imageUri.value != null) {
                capturedImagePath = imageUri.value.toString()

                val inputStream = context.contentResolver.openInputStream(imageUri.value!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val image = InputImage.fromBitmap(bitmap, 0)
                val labeler = ImageLabeling.getClient(
                    ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build()
                )

                isLoadingPrediction = true

                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        predictedLabel = labels.firstOrNull()?.text
                        isLoadingPrediction = false
                        showNewProductDialog = true
                    }
                    .addOnFailureListener { e ->
                        Log.e("ScannerFlow", "❌ Labeling failed: ${e.message}")
                        predictedLabel = null
                        isLoadingPrediction = false
                        showNewProductDialog = true
                    }
            }
        }
    )

    LaunchedEffect(shouldLaunchCamera) {
        if (shouldLaunchCamera && imageUri.value != null) {
            delay(300) // Small delay to ensure URI is ready
            takePictureLauncher.launch(imageUri.value)
            shouldLaunchCamera = false
        }
    }


    // When new product dialog is open
    if (showNewProductDialog && !isAnyDialogOpen && service != null) {
        isAnyDialogOpen = true
        ProductEntryDialog(
            barcode = newScannedBarcode ?: "",
            predictedLabel = predictedLabel,
            imagePath = capturedImagePath,
            onSubmit = { name, price, quantity ->
                Log.d("DialogFlow", "📤 Submitting new product to cart & sheets")
                val newProduct = Product(
                    barcode = newScannedBarcode ?: "",
                    name = name,
                    price = price,
                    quantity = quantity,
                    imagePath = capturedImagePath
                )
                coroutineScope.launch {
                    addNewProductToGoogleSheets(context, newProduct)
                }
                onCartItemsChange(cartItems + newProduct) // 🛒 add to cart
                showNewProductDialog = false
                isAnyDialogOpen = false
                scannerActive = true // ✅ RESUME scanner after dialog closed
            },
            onDismiss = {
                showNewProductDialog = false
                isAnyDialogOpen = false
                scannerActive = true // ✅ RESUME here as well
            },
            coroutineScope = coroutineScope,
            service = service
        )
    }


    fun recalculateTotalAmount() {
        var runningTotal = 0.0
        cartItemsState.forEach { item ->
            val price = item.price.toDoubleOrNull() ?: 0.0
            val subtotal = price * item.quantity
            Log.d(
                "TotalCalc",
                "🧾 Item: ${item.name}, Raw Price: '${item.price}', Parsed Price: $price, Quantity: ${item.quantity}, Subtotal: $subtotal"
            )
            runningTotal += subtotal
        }

        totalAmountState = runningTotal
        Log.d("TotalCalc", "➡️ Total amount from Recalculate Function: $runningTotal")

    }


    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }


    fun handleScannedBarcode(scannedBarcode: String,
                             setCurrentScreen: (String) -> Unit,   // <-- add this
                             scannerSource: String ) {
        if (!isProductListLoaded) {
            Log.w("ScannerFlow", "⚠️ Scan ignored — product list not loaded yet!")
            return
        }

        val currentTime = System.currentTimeMillis()
        if (scannedBarcode == lastUpdatedBarcode && currentTime - barcodeDetectedTime < 2000) {
            Log.d("ScannerFlow", "⏱️ Ignored duplicate scan: $scannedBarcode")
            return
        }

        barcodeDetectedTime = currentTime
        lastUpdatedBarcode = scannedBarcode

        Log.d("ScannerFlow", "📸 Scanned barcode: $scannedBarcode")
        scannerActive = false

        // ✅ Play beep
        try {
            val afd = context.resources.openRawResourceFd(R.raw.beepsound)
            val player = MediaPlayer()
            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            player.prepare()
            player.start()
            Log.d("ScannerFlow", "✅ Beep sound played")
        } catch (e: Exception) {
            Log.e("ScannerFlow", "❌ Failed to play sound: ${e.message}")
        }

        // 1️⃣ Already in cart?
        val existingProduct = cartItems.find { it.barcode == scannedBarcode }
        if (existingProduct != null) {
            Log.d("ScannerFlow", "🛒 Product already in cart: ${existingProduct.name}")

            val updatedList = cartItemsState.map {
                if (it.barcode == scannedBarcode) it.copy(quantity = it.quantity + 1) else it
            }

            cartItemsState.clear()
            cartItemsState.addAll(updatedList)
            onCartItemsChange(updatedList)

            scannedBarcodeToResume = scannedBarcode
            return
        }

        // 2️⃣ Check known products
        Log.d("ScannerFlow", "🔍 Checking against localProductList...")
        val knownProduct = localProductList.find { it.barcode.trim() == scannedBarcode.trim() }
        Log.d("ScannerFlow", "🔍 Local product match: ${knownProduct?.name ?: "❌ Not found"}")

        if (knownProduct != null) {
            Log.d("ScannerFlow", "✅ Product found in list: ${knownProduct.name}")

            val existing = cartItemsState.find { it.barcode == knownProduct.barcode }
            val updatedList = if (existing != null) {
                cartItemsState.map {
                    if (it.barcode == knownProduct.barcode) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                cartItemsState + knownProduct.copy(quantity = 1)
            }

            cartItemsState.clear()
            cartItemsState.addAll(updatedList)
            onCartItemsChange(updatedList)

            var runningTotal = 0.0
            cartItemsState.forEach { item ->
                val price: Double = item.price.toDoubleOrNull() ?: 0.0
                val subtotal: Double = price * item.quantity

                // 🔍 LOG GOES HERE
                Log.d(
                    "TotalCalc",
                    "🧾 Item: ${item.name}, Raw Price: '${item.price}', Parsed Price: $price, Quantity: ${item.quantity}, Subtotal: $subtotal"
                )

                runningTotal += subtotal
            }

            totalAmountState = runningTotal

            Log.d("TotalCalc", "➡️ Total amount from Handled Scanned Barcode: $runningTotal")


            scannedBarcodeToResume = scannedBarcode
            return
        }

// 3️⃣ Not found – fallback to manual entry dialog instead of launching camera
        Log.w("ScannerFlow", "⚠️ Product NOT found. Opening Product Entry Dialog...")

        newScannedBarcode = scannedBarcode
        predictedLabel = null
        savedImagePath = null
        showProductDialog = true
        scannerActive = false

        setCurrentScreen("Refreshing")
        coroutineScope.launch {
            delay(100)
            setCurrentScreen(if (scannerSource == "Sales") "Scanner" else "InventoryScanner")
        }

    }


    LaunchedEffect(cartItemsState) {
        var runningTotal = 0.0
        cartItemsState.forEach { item ->
            val price: Double = item.price.toDoubleOrNull() ?: 0.0
            val subtotal: Double = price * item.quantity

            // 🔍 LOG GOES HERE
            Log.d(
                "TotalCalc",
                "🧾 Item: ${item.name}, Raw Price: '${item.price}', Parsed Price: $price, Quantity: ${item.quantity}, Subtotal: $subtotal"
            )

            runningTotal += subtotal
        }

        totalAmountState = runningTotal

        Log.d(
            "TotalCalc",
            "➡️ Total amount from Launched Effect for Cart Items State 2: $runningTotal"
        )
    }


    scannedBarcodeToResume?.let {
        LaunchedEffect(it) {
            delay(500)
            scannerActive = true
            scannedBarcodeToResume = null
        }
    }


    fun handleCheckout() {

        val total = totalAmountState
        val tendered = amountTenderedState.toDoubleOrNull() ?: 0.0
        val change = changeState

        var iouName: String? = null
        if (tendered < total) {
            showIouDialog = true
            pendingCheckout = true
            return // Wait for input before proceeding
        }


        coroutineScope.launch {
            if (cartItemsState.isNotEmpty()) {
                cartItemsState.forEach { item ->
                    Log.d(
                        "🧾SaleLog",
                        "Writing: ${item.name}, Qty: ${item.quantity}, Price: ${item.price}"
                    )
                    service?.let { nonNullService ->
                        writeSaleToGoogleSheets(
                            barcode = item.barcode,
                            name = item.name,
                            price = item.price,
                            quantity = item.quantity,
                            imagePath = item.imagePath,
                            service = nonNullService,
                            coroutineScope = coroutineScope,
                            totalAmount = total,
                            amountTendered = tendered,
                            change = change,
                            iouName = iouName
                        )
                    }
                }

                cartItemsState.clear()
                totalAmountState = 0.0
                amountTenderedState = ""
                changeState = 0.00

                Log.d("salesScreen", "✅ Sale completed and cart cleared.")
            } else {
                Log.d("salesScreen", "⚠️ No items in cart to checkout.")
            }
        }
    }

    if (isLoadingPrediction) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Analyzing Image...") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Please wait while we try to recognize the product.")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

    fun proceedWithCheckout(iouName: String?) {
        coroutineScope.launch {
            if (cartItemsState.isNotEmpty()) {
                val total = totalAmountState
                val tendered = amountTenderedState.toDoubleOrNull() ?: 0.0
                val change = changeState

                cartItemsState.forEach { item ->
                    Log.d(
                        "🧾SaleLog",
                        "Writing: ${item.name}, Qty: ${item.quantity}, Price: ${item.price}"
                    )
                    service?.let { nonNullService ->
                        writeSaleToGoogleSheets(
                            barcode = item.barcode,
                            name = item.name,
                            price = item.price,
                            quantity = item.quantity,
                            imagePath = item.imagePath,
                            service = nonNullService,
                            coroutineScope = coroutineScope,
                            totalAmount = total,
                            amountTendered = tendered,
                            change = change,
                            iouName = iouName
                        )
                    }
                }

                cartItemsState.clear()
                totalAmountState = 0.0
                amountTenderedState = ""
                changeState = 0.0
                Log.d("salesScreen", "✅ Sale completed and cart cleared.")
            } else {
                Log.d("salesScreen", "⚠️ No items in cart to checkout.")
            }
        }
    }


    if (showIouDialog) {
        AlertDialog(
            onDismissRequest = {
                showIouDialog = false
                pendingCheckout = false
            },
            title = { Text("Enter IOU Name") },
            text = {
                Column {
                    Text("Customer still owes money. Please enter their name:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = iouNameInput,
                        onValueChange = { iouNameInput = it },
                        placeholder = { Text("Customer Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showIouDialog = false
                        if (pendingCheckout) {
                            proceedWithCheckout(iouNameInput)
                            pendingCheckout = false
                        }
                    }
                ) {
                    Text("Save & Continue")
                }
            }
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(innerPadding)
            .padding(8.dp)
    ) {
        // 🖼️ Top section: Scanner + Cart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {
            // 📷 Scanner
            // 🖼️ Top section: Scanner + Cart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                // 📷 Scanner + Amount Section on Left
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .background(Color.DarkGray)
                ) {
                    // 📸 Scanner Preview at the top
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp)
                            .background(Color.DarkGray)
                    ) {
                        // 🔍 Scanner Preview
                        ScannerPreview(
                            onBarcodeDetected = { scannedBarcode ->
                                handleScannedBarcode(scannedBarcode, setCurrentScreen, scannerSource)
                                coroutineScope.launch {
                                    delay(1000)
                                    scannerActive = true
                                }
                            },
                            scannerActive = scannerActive,
                            onPauseScanner = { scannerActive = false },
                            onCameraControlReady = { control ->
                                Log.d("FLASH", "📥 Received camera control in SalesScreen")
                                onCameraControlReady(control)
                            }
                        )

                        // 🔦 Flashlight Toggle Button 1
                        IconButton(
                            onClick = {
                                isTorchOn = !isTorchOn
                                cameraControl?.enableTorch(isTorchOn)
                                    ?: Log.e("FLASH", "⚠️ Camera control is NULL — torch not toggled")
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd) // ✅ now valid inside Box
                                .padding(12.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                                contentDescription = "Toggle Flashlight",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 💵 Amount Input + Totals
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = amountTenderedState,
                            onValueChange = {
                                amountTenderedState = it
                                onAmountChange(it)
                            },
                            placeholder = { Text("Enter Amount", color = Color.Gray) },
                            modifier = Modifier
                                .weight(0.5f)
                                .background(Color.White),
                            textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Text(
                            text = "Total: R${"%.2f".format(totalAmountState)}",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(0.25f)
                        )

                        Text(
                            text = "Change: R${"%.2f".format(changeState)}",
                            color = if (changeState < 0) Color.Red else Color.Green,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(0.25f)
                        )
                    }

                    // 💸 Denomination Buttons Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            listOf(
                                "0.50",
                                "1",
                                "2",
                                "5",
                                "10",
                                "20",
                                "50",
                                "100",
                                "200"
                            )
                        ) { note ->
                            var isPressed by remember { mutableStateOf(false) }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .border(1.dp, Color(0xFF555555), RoundedCornerShape(6.dp))
                                    .background(if (isPressed) Color.White else Color(0xFF444444))
                                    .clickable {
                                        val current = amountTenderedState.toDoubleOrNull() ?: 0.0
                                        val added: Double =
                                            current + note.replace(",", ".").toDouble()
                                        amountTenderedState =
                                            String.format(Locale.US, "%.2f", added)
                                        onAmountChange(amountTenderedState)
                                        isPressed = true
                                        coroutineScope.launch {
                                            delay(150)
                                            isPressed = false
                                        }
                                    }
                                    .padding(vertical = 6.dp)
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "R$note",
                                    color = if (isPressed) Color.Black else Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

            }


            // 🛒 Cart
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF2C2C2C))
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Sales",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "Shopping Cart",
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItemsState, key = { it.barcode }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 🖼️ Always reserve space for the image
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.DarkGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // 🖼️ Product Image - Reliable loading with fallback
                                    val imageBitmap: ImageBitmap? = remember(item.imagePath) {
                                        item.imagePath?.let { safePath ->
                                            try {
                                                val file = File(safePath)
                                                Log.d(
                                                    "🖼️ImageCheck",
                                                    "Checking file: ${file.absolutePath}"
                                                )
                                                if (file.exists()) {
                                                    BitmapFactory.decodeFile(file.absolutePath)
                                                        ?.asImageBitmap()
                                                } else {
                                                    Log.w("🖼️ImageCheck", "File does NOT exist!")
                                                    null
                                                }
                                            } catch (e: Exception) {
                                                Log.e(
                                                    "🖼️ImageError",
                                                    "Error loading image: ${e.message}"
                                                )
                                                null
                                            }
                                        }
                                    }


                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.DarkGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (imageBitmap != null) {
                                            Image(
                                                bitmap = imageBitmap,
                                                contentDescription = item.name,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .border(
                                                        1.dp,
                                                        Color.Gray,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Image,
                                                contentDescription = "No Image",
                                                tint = Color.Gray
                                            )
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp, end = 8.dp)
                                ) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                    val priceDouble = item.price.toDoubleOrNull() ?: 0.0
                                    Text(
                                        text = "R${"%.2f".format(priceDouble)} x ${item.quantity}",
                                        fontSize = 16.sp,
                                        color = Color.LightGray
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        val updatedList = cartItemsState.map {
                                            if (it.barcode == item.barcode) it.copy(quantity = item.quantity - 1) else it
                                        }.filter { it.quantity > 0 }
                                        cartItemsState.clear()
                                        cartItemsState.addAll(updatedList)
                                        onQuantityChange(item, item.quantity - 1)
                                        recalculateTotalAmount()
                                    }) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowDown,
                                            contentDescription = "Decrease"
                                        )
                                    }

                                    Text(
                                        text = "${item.quantity}",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )

                                    IconButton(onClick = {
                                        val updatedList = cartItemsState.map {
                                            if (it.barcode == item.barcode) it.copy(quantity = item.quantity + 1) else it
                                        }
                                        cartItemsState.clear()
                                        cartItemsState.addAll(updatedList)
                                        onQuantityChange(item, item.quantity + 1)
                                        recalculateTotalAmount()
                                    }) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowUp,
                                            contentDescription = "Increase"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🖐️ Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val total = totalAmountState
                    val tendered = amountTenderedState.toDoubleOrNull() ?: 0.0

                    coroutineScope.launch {
                        service?.let {
                            cartItemsState.forEach { product ->
                                writeToGoogleSheets(
                                    barcode = product.barcode,
                                    name = product.name,
                                    price = product.price,
                                    quantity = product.quantity,
                                    date = getTodayDate(),
                                    time = getCurrentTime(),
                                    service = it,
                                    coroutineScope = coroutineScope,
                                    imagePath = product.imagePath
                                )
                            }
                            updateInventoryQuantitiesAfterSale(cartItemsState, "Sheet1", it)
                        }
                        cartItemsState.clear()
                        amountTenderedState = ""
                        recalculateTotalAmount()
                        Toast.makeText(
                            context,
                            "✅ Sale completed & stock updated.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    if (tendered < total) {
                        showIouDialog = true
                        pendingCheckout = true
                    } else {
                        proceedWithCheckout(null)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                enabled = cartItemsState.isNotEmpty() // optional: disables if cart is empty
            ) {
                Text("✅ Complete Sale", color = Color.Black)
            }


            Button(
                onClick = { refreshedProductList() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("🔄 Refresh List", color = Color.Black)
            }

            Button(
                onClick = {
                    cartItemsState.clear()
                    amountTenderedState = ""
                    onAmountChange("") // just in case it's wired to recalculate
                    totalAmountState = 0.0
                    changeState = 0.0
                    onClear() // optional: keep if it does something else useful
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("❌ Cancel Sale", color = Color.Black)
            }

        }
    }
}




    suspend fun writeSaleToGoogleSheets(
        barcode: String,
        name: String,
        price: String,
        quantity: Int,
        imagePath: String?,  // new param
        service: Sheets,
        coroutineScope: CoroutineScope,
        totalAmount: Double,
        amountTendered: Double,
        change: Double,
        iouName: String? = null
    ) {
        try {
            val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA"
            val range = "Sales_Log!A:L"

            // Get the date and time
            val now = Calendar.getInstance()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now.time)
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(now.time)

            val subtotal = (price.toDoubleOrNull() ?: 0.0) * quantity

            val values: List<List<String>> = listOf(
                listOf(
                    date,
                    time,
                    barcode,
                    name,
                    quantity.toString(),
                    price,
                    subtotal.toString(),
                    imagePath ?: "",
                    totalAmount.toString(),
                    amountTendered.toString(),
                    change.toString(),
                    iouName ?: ""
                )
            )

            val body = ValueRange().setValues(values)

            withContext(Dispatchers.IO) {
                service.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute()
            }

            Log.d("writeSale", "✅ Sale written to Sales_Log for barcode: $barcode")
        } catch (e: Exception) {
            Log.e("writeSale", "❌ Error writing sale: ${e.localizedMessage}")
        }
    }


    suspend fun loadProductListFromGoogleSheets(context: Context): List<Product> {
        return withContext(Dispatchers.IO) {
            val productList: MutableList<Product> = mutableListOf()

            try {
                val service = (context as? MainActivity)?.getSheetsService(context)
                if (service != null) {
                    val spreadsheetId = "1KNHlGEKC1Oz_wf7NT5w4XKVIqMiLIzrisHiVIUkz-nA"
                    val range = "Sheet1!A2:G"

                    Log.d("ProductLoader", "✅ Google Sheets service is available")

                    val response = service.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute()
                    val values = response.getValues()

                    Log.d("ProductLoader", "📦 Retrieved ${values?.size ?: 0} rows from Sheet")

                    if (values != null) {
                        for (row in values) {
                            val barcode = row.getOrNull(0)?.toString() ?: ""
                            val name = row.getOrNull(1)?.toString() ?: ""
                            val price = row.getOrNull(2)?.toString() ?: ""
                            val quantity = row.getOrNull(3)?.toString()?.toIntOrNull() ?: 0
                            val imagePath = row.getOrNull(4)?.toString()

                            if (barcode.isNotEmpty()) {
                                Log.d(
                                    "ProductLoader",
                                    "🔍 Parsed product — barcode: $barcode, name: $name, price: $price, qty: $quantity, image: $imagePath"
                                )

                                productList.add(
                                    Product(
                                        barcode = barcode,
                                        name = name,
                                        price = price,
                                        quantity = quantity,
                                        imagePath = imagePath
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ProductLoader", "❌ Error loading products: ${e.localizedMessage}", e)
                e.printStackTrace()
            }

            Log.d("ProductLoader", "✅ Final productList size: ${productList.size}")
            return@withContext productList
        }
    }




