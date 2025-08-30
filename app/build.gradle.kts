// ✅ Application-Level Gradle File
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") // ✅ Google Services for Firebase

}

android {
    namespace = "com.example.qrcodescannertuckshop2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.qrcodescannertuckshop2"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    android {

            packaging {
                resources {
                    excludes += "META-INF/DEPENDENCIES"
                    excludes += "META-INF/INDEX.LIST"
                }
            }

    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // ✅ Use Java 17 for latest support
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" // ✅ Match Java 17 compatibility
    }

    buildFeatures {
        compose = true // ✅ Ensure Jetpack Compose is enabled
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15" // ✅ Latest Jetpack Compose Compiler Dealing with a warning
    }
}

dependencies {
    // ✅ Core Android Libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.accompanist:accompanist-flowlayout:0.28.0")



    //implementation("androidx.compose.compiler:compiler:1.5.15")

    // ✅ Jetpack Compose Dependencies
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // ✅ CameraX for QR Code Scanning
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")

    // ✅ ML Kit Barcode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.0.3")

    // ✅ Firebase Dependencies (Ensure Versioning Matches)
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // ✅ Google Sign-In (Updated to GIS SDK)
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-identity:18.0.1") // ✅ Required for GIS Authentication

    // ✅ Google API Client dependencies
    implementation("com.google.api-client:google-api-client-android:1.33.2")
    implementation("com.google.api-client:google-api-client-gson:1.33.2") // Keep the latest
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation("com.google.http-client:google-http-client-android:1.43.3")
    implementation("com.google.http-client:google-http-client:1.43.3")


    // ✅ Google Sheets API
    implementation("com.google.apis:google-api-services-sheets:v4-rev612-1.25.0")

    // ✅ Google OAuth dependencies (Fix potential issues)
    implementation("com.google.auth:google-auth-library-oauth2-http:1.21.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.21.0")


    // ✅ Try updating or downgrading if needed (only if the error persists)
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.3")




    implementation(libs.googleid) // Google Sheets API
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation(libs.espresso.core)
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")
    implementation(libs.image.labeling.default.common)
    implementation(libs.image.labeling.common)
    implementation ("com.google.mlkit:image-labeling:17.0.7")



    testImplementation("junit:junit:4.13.2") // JUnit for Unit tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // AndroidX JUnit
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso for UI tests

    // 👇 ML Kit Text Recognition (on-device)
    implementation ("com.google.mlkit:text-recognition:16.0.0")

}