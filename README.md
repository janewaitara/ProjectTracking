# ProjectTracking

A Jetpack Compose App which allows users(humans) to track their projects progress. The app allows the user to break down their projects into easier manageable tasks where they can easily update their tasks' progress.

# Designs
There is a slight mismatch between the designs and the app implementation: The app does not have project's collaboration - no team members.


<p align="left">
 <img src="https://user-images.githubusercontent.com/32500878/209720513-cda40a4e-854f-40e7-9d09-5be59531af3d.png" width=30% height=30%> &nbsp;&nbsp;&nbsp;&nbsp;
 <img src="https://user-images.githubusercontent.com/32500878/209720755-3c1759c6-02c8-44ad-ab1a-09a2826e9530.png" width=30% height=30%>
&nbsp;&nbsp;&nbsp;&nbsp;
 <img src="https://user-images.githubusercontent.com/32500878/209724024-274e86af-5fe0-4760-a8ea-0f5432858159.png" width=30% height=30%>
</p>

<p align="left">
 <img src="https://user-images.githubusercontent.com/32500878/209720692-c9d80c45-aef1-4ad1-95be-9cf36207f358.png" width=30% height=30%> &nbsp;&nbsp;&nbsp;&nbsp;
 <img src="https://user-images.githubusercontent.com/32500878/209723542-c855577b-3d08-41a0-88d1-025a415ee69b.png" width=30% height=30%> &nbsp;&nbsp;&nbsp;&nbsp;
 <img src="https://user-images.githubusercontent.com/32500878/209723860-0fe9f49f-db2a-4632-8187-9a3dfb6b3b6d.png" width=30% height=30%>
</p>

<p align="left">
 
</p>

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

