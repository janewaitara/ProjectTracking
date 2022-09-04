package com.mumbicodes.domain.util

sealed class ProgressStatus(val status: String) {
    class NotStarted(status: String) : ProgressStatus(status)
    class InProgress(status: String) : ProgressStatus(status)
    class Completed(status: String) : ProgressStatus(status)
}
