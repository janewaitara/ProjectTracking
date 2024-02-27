package com.mumbicodes.projectie.data.helpers

sealed interface LocalResult<out T> {
    data class Success<T>(val data: T) : LocalResult<Nothing>
    data class Error(val errorMessage: String) : LocalResult<Nothing>
}
