plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("androidx.room") // only for schema generation
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

    // let Room write schemas into /app/schemas for androidTest
    sourceSets["androidTest"].assets.srcDir("$projectDir/schemas")

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.15" } // OK for Kotlin 1.9.25
}

dependencies {
    implementation(libs.googleid)
    // ----- ROOM (Kotlin 1.9-compatible) -----
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // Do NOT add androidx.sqlite 2.5+; Room 2.6.1 will pull the right 2.4.x

    // ----- Core Android -----
    implementation("androidx.core:core-ktx:1.15.0")      // keep only this (remove 1.12.0)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ----- Compose -----
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Images / layout helpers
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.accompanist:accompanist-flowlayout:0.28.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // CameraX
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")

    // ML Kit
    implementation("com.google.mlkit:barcode-scanning:17.0.3")
    implementation("com.google.mlkit:image-labeling:17.0.7")
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // Firebase (BOM keeps versions aligned)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Google Sign-In / Credentials
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-identity:18.0.1")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")

    // Google / HTTP clients
    implementation("com.google.api-client:google-api-client-android:1.33.2")
    implementation("com.google.api-client:google-api-client-gson:1.33.2")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation("com.google.http-client:google-http-client-android:1.43.3")
    implementation("com.google.http-client:google-http-client:1.43.3")
    implementation("com.google.apis:google-api-services-sheets:v4-rev612-1.25.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.21.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Let the Room Gradle plugin write schemas here
room {
    schemaDirectory("$projectDir/schemas")
}

// (Optional but helpful) prevent accidental Kotlin 2.x or Room 2.8.x via transitive deps
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.9.25")
        }
        if (requested.group == "androidx.room") {
            useVersion("2.6.1")
        }
    }
}
