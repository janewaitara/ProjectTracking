object Versions {

    //Version codes for all the libraries
    const val androidApplication = "7.2.1"
    const val androidLibrary = "7.2.1"
    const val kotlin = "1.7.0"

    const val appCompat = "1.4.1"
    const val ktx = "1.7.0"

    //Version codes for all the test libraries
    const val junit4 = "4.13.2"
    const val junit = "1.1.3"
    const val testRunner = "1.4.1-alpha03"
    const val espresso = "3.5.0-alpha03"

    //compose
    const val composeUi = "1.2.0-alpha08"
    const val composeMaterial3 = "1.0.0-alpha14"
    const val constraintLayoutCompose = "1.0.0-beta02"
    const val activityCompose = "1.3.1"

    // Lifecycle
    const val lifecycle = "2.4.1"

}

object BuildPlugins {
    //All the build plugins are added here
    const val androidLibrary = "com.android.library"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
}

object Libraries {
    //Any Library is added here
    const val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"

    //compose
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeUi}"
    const val composeTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUi}"
    const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    const val composeMaterial3Window =
        "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayoutCompose}"

    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
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
    const val compileSdkVersion = 32
    const val targetSdkVersion = compileSdkVersion
    const val versionCode = 1
    const val versionName = "1.0"
}

