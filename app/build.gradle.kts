plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt") // Apply kapt for data binding
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" // Ensure it's compatible with Kotlin 2.0.0
}

android {
    namespace = "com.example.mypersonalassistant"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mypersonalassistant"
        minSdk = 21
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.9"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // GSON for JSON parsing
    implementation(libs.gson)

    // Logging Interceptor for HTTP request/response logging
    implementation(libs.logging.interceptor)

    // Moshi library and converter for JSON serialization
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // For coroutine support
    implementation(libs.kotlinx.coroutines.android)

    // Material Design Components
    implementation(libs.material)
    implementation(libs.androidx.material3)

    // Jetpack Compose dependencies
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Annotation Processing using KAPT for Data Binding
    kapt(libs.androidx.databinding.compiler)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
