plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    // id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.yuruneji.camera_training"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yuruneji.camera_training"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_URL_BASE", "\"${project.properties["API_URL_BASE"]}\"")
        buildConfigField("String", "API_URL_DEVELOP", "\"${project.properties["API_URL_DEVELOP"]}\"")
        buildConfigField("String", "API_URL_STAGING", "\"${project.properties["API_URL_STAGING"]}\"")
        buildConfigField("String", "API_URL_PRODUCTION", "\"${project.properties["API_URL_PRODUCTION"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")
    // implementation("androidx.recyclervew:recyclerview:1.3.2")


    // DI: Hilt
    val hilt_version = "2.49"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Compose Navigation
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // DB: Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // ksp("androidx.room:room-compiler:2.5.0")
    implementation("androidx.room:room-ktx:$room_version")

    // Camera: CameraX
    val camerax_version = "1.4.0-beta02"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    // mobile SDK: ML Kit
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.mlkit:face-detection:16.1.5")

    // WebServer: NanoHttpd
    implementation("org.nanohttpd:nanohttpd-webserver:2.3.1")

    // HTTP Client:Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")

    // Json: Moshi
    val moshi_version = "1.14.0"
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")

    // Image: Coil
    val coil_version = "2.4.0"
    implementation("io.coil-kt:coil-compose:$coil_version")

    // Log: Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Crypto: Security-Crypto
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Apache Commons Lang 3.12.0
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // Apache Commons Codec 1.15
    implementation("commons-codec:commons-codec:1.15")
    // Apache Commons Net 3.11.1
    implementation("commons-net:commons-net:3.11.1")

    // Location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Debug: LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

}
