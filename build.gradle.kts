// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id (BuildPlugins.androidApplication) version Versions.androidApplication apply false
    id (BuildPlugins.androidLibrary) version Versions.androidLibrary apply false
    id (BuildPlugins.kotlinAndroid) version Versions.kotlin apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

