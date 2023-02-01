package com.mumbicodes.projectie.presentation.add_edit_milestone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class TaskTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

class TaskState(
    val milestoneId: Int,
    val taskId: Int,
    initialTaskTitleState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Title"
    ),
    initialTaskDescState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Description"
    ),
    initialStatusState: Boolean = false,
) {
    var taskTitleState by mutableStateOf(
        initialTaskTitleState
    )
    var taskDescState by mutableStateOf(
        initialTaskDescState
    )
    var statusState by mutableStateOf(initialStatusState)
}
