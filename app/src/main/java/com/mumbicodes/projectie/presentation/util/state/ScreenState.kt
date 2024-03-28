package com.mumbicodes.projectie.presentation.util.state

sealed interface ScreenState<out T> {
    object Loading : ScreenState<Nothing>
    object Empty : ScreenState<Nothing>
    data class Data<T>(val data: T) : ScreenState<T>
}

sealed interface ListState<out T> {
    object Loading : ListState<Nothing>
    data class Success<T>(val data: SuccessState<T>) : ListState<T>
    data class Error(val errorMessage: String) : ListState<Nothing>
}

sealed interface SuccessState<out T> {
    object Empty : SuccessState<Nothing>
    data class Data<T>(val data: T) : SuccessState<T>
}
