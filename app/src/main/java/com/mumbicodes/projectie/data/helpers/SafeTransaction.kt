package com.mumbicodes.projectie.data.helpers

fun <T> safeTransaction(
    block: suspend () -> T,
): LocalResult<T> =
    try {
        LocalResult.Success(block)
    } catch (exception: Exception) {
        LocalResult.Error(exception.localizedMessage ?: "There was an error received")
    }
