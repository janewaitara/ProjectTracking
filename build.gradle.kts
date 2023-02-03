// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(BuildPlugins.androidApplication) version Versions.androidApplication apply false
    id(BuildPlugins.androidLibrary) version Versions.androidLibrary apply false
    id(BuildPlugins.kotlinAndroid) version Versions.kotlin apply false
    id(BuildPlugins.ktlintPlugin) version Versions.ktlint
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

buildscript {
    dependencies {
        classpath(Libraries.hiltGradle)
        classpath(Libraries.googleServices)
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    }
}

allprojects {

    apply(plugin = BuildPlugins.ktlintPlugin)
    ktlint {
        android.set(false)
        verbose.set(false)
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
        disabledRules.set(setOf("no-wildcard-imports"))
    }
}
