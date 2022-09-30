package com.mumbicodes.presentation.add_edit_milestone

data class TaskTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

data class TaskState(
    // TODO add an Id
    val taskId: Int? = null,
    var taskTitleState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Title"
    ),
    var taskDescState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Description"
    ),
    var statusState: Boolean = false,
)
