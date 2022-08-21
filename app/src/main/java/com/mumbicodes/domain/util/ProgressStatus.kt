package com.mumbicodes.domain.util

sealed class ProgressStatus {
    class NotStarted(status: String) : ProgressStatus()
    class InProgress(status: String) : ProgressStatus()
    class Completed(status: String) : ProgressStatus()
}
