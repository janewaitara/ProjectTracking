plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.ktlintPlugin)
    id(BuildPlugins.hiltPlugin)
    id("kotlin-kapt")
    kotlin("kapt")
}

android {
    compileSdk = AndroidSdk.compileSdkVersion

    defaultConfig {
        applicationId = "com.mumbicodes"
        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeUi
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Libraries.ktxCore)
    implementation(Libraries.composeUi)
    implementation(Libraries.composeMaterial3)
    implementation(Libraries.composeMaterial3Window)
    implementation(Libraries.composeTooling)
    implementation(Libraries.activityCompose)
    implementation(Libraries.constraintLayoutCompose)

    // Lifecycle
    implementation(Libraries.lifecycle)
    implementation(Libraries.lifecycleViewModel)

    // Room
    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)
    implementation(Libraries.roomKtx)

    // Coroutines
    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)

    // Navigation
    implementation(Libraries.navigationCompose)

    // DI - Hilt
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompilerAndroid)
    kapt(Libraries.hiltCompiler)
    implementation(Libraries.hiltNavigation)
    implementation(Libraries.hiltViewModel)

    // Unit tests
    testImplementation(TestLibraries.junit4)

    // Android Tests
    androidTestImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.espresso)
    androidTestImplementation(TestLibraries.composeJunit4)

    debugImplementation(TestLibraries.composeTooling)
    debugImplementation(TestLibraries.composeManifest)
}
