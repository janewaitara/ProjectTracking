package com.mumbicodes.presentation.add_edit_milestone

data class TaskTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

data class TaskState(
    // TODO add an Id
    val taskTitleState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Title"
    ),
    val taskDescState: TaskTextFieldState = TaskTextFieldState(
        hint = "Task Description"
    ),
    val statusState: Boolean = false,
)
