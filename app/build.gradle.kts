plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

}

android {
    namespace = "com.example.leostoryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.leostoryapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Core Android Libraries
    implementation(libs.androidx.core.ktx) // Core KTX untuk sintaks Kotlin yang lebih ringkas
    implementation(libs.androidx.appcompat) // Dukungan untuk fitur modern pada perangkat lama
    implementation(libs.androidx.constraintlayout) // Layout yang fleksibel dan optimal

    // Material Design
    implementation(libs.material) // Komponen Material Design seperti tombol, input, dsb.

    // Android Activity & Lifecycle
    implementation(libs.androidx.activity) // Dukungan Activity modern
    implementation(libs.androidx.activity.ktx) // Sintaks tambahan untuk Activity modern
    implementation(libs.androidx.lifecycle.livedata.ktx) // LiveData untuk pengelolaan data UI
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel untuk pengelolaan UI logic

    // Camera & Maps
    implementation(libs.androidx.camera.view) // Dukungan kamera modern
    implementation(libs.play.services.maps) // Google Maps SDK
    implementation(libs.play.services.location) // Layanan lokasi Google

    // RecyclerView
    implementation(libs.androidx.recyclerview) // RecyclerView untuk menampilkan daftar

    // Room Database
    implementation(libs.androidx.room.runtime) // Runtime library Room
    implementation(libs.androidx.room.ktx) // Sintaks tambahan untuk Room
    implementation(libs.androidx.room.paging) // Integrasi Room dengan Paging
    ksp (libs.androidx.room.compiler)

    // Paging Library
    implementation(libs.androidx.paging.runtime.ktx) // Paging untuk pengelolaan data besar
    implementation(libs.androidx.paging.common.ktx) // Dukungan Paging untuk pengujian

    // Networking
    implementation(libs.retrofit) // Library Retrofit untuk HTTP client
    implementation(libs.retrofit2.converter.gson) // Alternatif konverter JSON
    implementation(libs.okhttp) // OkHttp untuk HTTP client
    implementation(libs.logging.interceptor) // Logging untuk debugging jaringan
    implementation(libs.android.async.http) // Library HTTP asynchronous

    // Image Loading
    implementation(libs.glide) // Library untuk memuat dan menampilkan gambar
    implementation(libs.gson) // Parser JSON menggunakan Gson

    // DataStore & Coroutines
    implementation(libs.androidx.datastore.preferences) // DataStore untuk penyimpanan data key-value
    implementation(libs.kotlinx.coroutines.core) // Core library untuk coroutines
    implementation(libs.kotlinx.coroutines.android) // Dukungan coroutines untuk Android

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx) // WorkManager untuk tugas latar belakang

    // Testing Dependencies
    testImplementation(libs.junit) // Framework untuk pengujian unit
    testImplementation(libs.mockito.core) // Mockito untuk mocking dalam pengujian
    testImplementation(libs.core.testing) // Core-testing untuk unit test dengan LiveData
    testImplementation(libs.kotlinx.coroutines.test) // Pengujian coroutine
    testImplementation(libs.mockk) // Mockk untuk mocking dalam Kotlin

    // Android Instrumented Testing
    androidTestImplementation(libs.androidx.junit) // Dukungan JUnit di Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso untuk pengujian UI
    androidTestImplementation(libs.androidx.core.testing) // Testing framework untuk Android
    androidTestImplementation(libs.kotlinx.coroutines.test) // Testing coroutine di Android

    // Paging Testing
    testImplementation(libs.androidx.paging.common) // Dukungan pengujian untuk Paging
    testImplementation(libs.androidx.paging.common.ktx) // Alternatif dukungan untuk Paging testing
    testImplementation (libs.androidx.paging.testing) // Sesuaikan versi Paging

}
