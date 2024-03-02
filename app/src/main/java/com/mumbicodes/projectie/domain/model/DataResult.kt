package com.mumbicodes.projectie.domain.model

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val errorMessage: String) : DataResult<Nothing>
}
