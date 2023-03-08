object Versions {

    //Version codes for all the libraries
    const val androidApplication = "7.2.1"
    const val androidLibrary = "7.2.1"
    const val kotlin = "1.7.10"

    const val appCompat = "1.4.1"
    const val ktx = "1.7.0"

    //Version codes for all the test libraries
    const val junit4 = "4.13.2"
    const val junit = "1.1.3"
    const val testRunner = "1.4.1-alpha03"
    const val espresso = "3.5.0-alpha03"

    //Compose
    const val composeUi = "1.3.0"
    const val composeMaterial3 = "1.0.0-alpha14"
    const val composeMaterial2 = "1.2.1"
    const val constraintLayoutCompose = "1.0.0-beta02"
    const val activityCompose = "1.3.1"

    // Lifecycle
    const val lifecycle = "2.4.1"

    // Gradle Plugins - ktlint
    const val ktlint = "10.2.1"

    //Room
    const val room = "2.5.0"

    // Coroutines
    const val coroutines = "1.5.0"

    //Navigation
    const val navVersion = "2.5.1"

    // Hilt
    const val hilt = "2.42"
    const val hiltWithOtherLibs = "1.0.0"

    // Moshi
    const val moshi = "1.13.0"

    // Retrofit
    const val retrofit = "2.9.0"

    //Datastore
    const val dataStore = "1.0.0"

    //Accompanist
    const val accompanist = "0.28.0"

    //Splash screen
    const val splashScreen = "1.0.0"

    //LeakCanary
    const val leakCanary = "2.10"

    //gsm
    const val gsm = "4.3.15"

    //Firebase
    const val firebase = "31.2.0"

    //WorkManager
    const val workManager = "2.8.0"
}

object BuildPlugins {
    //All the build plugins are added here
    const val androidLibrary = "com.android.library"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val ktlintPlugin = "org.jlleitschuh.gradle.ktlint"
    const val hiltPlugin = "dagger.hilt.android.plugin"
    const val googleServices = "com.google.gms.google-services"
}

object Libraries {
    //Any Library is added here
    const val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"

    //Compose
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeUi}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.composeUi}"
    const val composeTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUi}"
    const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    const val composeMaterial3Window =
        "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val constraintLayoutCompose =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayoutCompose}"
    const val composeMaterial2 = "androidx.compose.material:material:${Versions.composeMaterial2}"
    const val materialWindowClassSize =
        "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"

    //Navigation
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navVersion}"

    //Lifecycle
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"

    //Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    // Coroutines
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Hilt
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompilerAndroid = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltWithOtherLibs}"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.hiltWithOtherLibs}"
    const val hiltWork = "androidx.hilt:hilt-work:${Versions.hiltWithOtherLibs}"

    // Moshi
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    //Datastore
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    //Pager and indicators
    const val pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    const val pagerIndicators =
        "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}"

    //Splash API
    const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"

    //LeakCanary
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    //Gsm
    const val googleServices = "com.google.gms:google-services:${Versions.gsm}"

    //Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics =  "com.google.firebase:firebase-analytics-ktx"
    const val firebasePerformance = "com.google.firebase:firebase-perf-ktx"

    //WorkManager
    const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"
}

object TestLibraries {
    //any test library is added here
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junit = "androidx.test.ext:junit:${Versions.junit}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val composeJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.composeUi}"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.composeUi}"
    const val composeManifest = "androidx.compose.ui:ui-test-manifest:${Versions.composeUi}"
}

object AndroidSdk {
    const val minSdkVersion = 21
    const val compileSdkVersion = 33
    const val targetSdkVersion = compileSdkVersion
    const val versionCode = 1
    const val versionName = "1.0"
}

