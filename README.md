# ProjectTracking

A Jetpack Compose App

# Architecture
The app uses MVVM (Model View View Model) architecture to have a unidirectional flow of data, separation of concern, testability, and a lot more.



# Tech Stack

- Tech Stack
  - [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
  - [Android Jetpack](https://developer.android.com/jetpack) https://developer.android.com/topic/libraries/data-binding/
    * [Room](https://developer.android.com/topic/libraries/architecture/room) - a persistence library provides an abstraction layer over SQLite.
    * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform action when lifecycle state changes.
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way.
  - [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Executing code asynchronously.
  - [Moshi](https://square.github.io/moshi/1.x/moshi/index.html) - A modern JSON library for Android, Java and Kotlin
  - [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - An asynchronous version of a Sequence, a type of collection whose values are lazily produced. Flow handles the stream of data asynchronously that executes sequentially.
  - [HILT](https://developer.android.com/training/dependency-injection/hilt-android) - a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.

- Gradle
  * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - For reference purposes, here's an [article](https://evanschepsiror.medium.com/migrating-to-kotlin-dsl-4ee0d6d5c977) explaining the migration.
  * Plugins
      - [Ktlint](https://github.com/JLLeitschuh/ktlint-gradle) - creates convenient tasks in your Gradle project that run ktlint checks or do code auto format.
      
- CI/CD
  * Github Actions

