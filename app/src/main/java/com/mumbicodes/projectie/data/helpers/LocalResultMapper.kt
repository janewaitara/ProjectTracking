package com.mumbicodes.projectie.data.helpers

import com.mumbicodes.projectie.domain.model.DataResult

fun <T> LocalResult<T>.toDataResult(): DataResult<T> = when (this) {
    is LocalResult.Error -> DataResult.Error(this.errorMessage)
    is LocalResult.Success -> DataResult.Success(this.data)
}
