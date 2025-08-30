pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") } // ✅ Keep existing JetBrains repo
        maven { url = uri("https://repo1.maven.org/maven2/") } // ✅ Added Maven Central repo for Google OAuth
        maven { url = uri("https://maven.google.com/")
            maven { url = uri("https://jitpack.io")}
        }
        }
    plugins {
        id("com.android.application") version "8.7.3" apply false
        id("com.android.library") version "8.3.2" apply false
        id("org.jetbrains.kotlin.android") version "1.9.25" apply false
        id("com.google.gms.google-services") version "4.4.2" apply false
        kotlin("jvm") version "1.9.25"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") } // ✅ Keep existing JetBrains repo
        maven { url = uri("https://repo1.maven.org/maven2/")}
            maven { url = uri("https://jitpack.io")}
            maven { url = uri("https://maven.google.com/")}// ✅ Added missing repo for Google OAuth
    }
}

rootProject.name = "QRCodeScannerTuckshop"
include(":app")
