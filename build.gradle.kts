// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false
    kotlin("jvm")
}

buildscript {
    val kotlinVersion = "1.9.25"

    //repositories {
        //google()
       // mavenCentral()
    //}

    dependencies {
        classpath("com.android.tools.build:gradle:8.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.4.2") // ✅ Firebase Services
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}



dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(8)
}