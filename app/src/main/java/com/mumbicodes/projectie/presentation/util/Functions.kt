package com.mumbicodes.projectie.presentation.util

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.presentation.add_edit_milestone.TaskState
import com.mumbicodes.projectie.presentation.add_edit_milestone.TaskTextFieldState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun provideFormatter(pattern: String) = DateTimeFormatter.ofPattern(pattern)

fun convertDateToString(localDate: LocalDate, pattern: String): String {
    val formatter = provideFormatter(pattern)
    return localDate.format(formatter)
}

fun convertStringToLocalDate(dateAsString: String, pattern: String): LocalDate {
    val formatter = provideFormatter(pattern)
    return LocalDate.parse(dateAsString, formatter)
}

fun convertDateFormatsStrings(
    dateAsString: String,
    oldPattern: String,
    newPattern: String,
): String {
    val localDate = convertStringToLocalDate(dateAsString, oldPattern)
    return convertDateToString(localDate, newPattern)
}

fun convertLocalDateToLong(localDate: LocalDate): Long = localDate.toEpochDay()
fun convertLongToLocalDate(time: Long): LocalDate = LocalDate.ofEpochDay(time)

fun numberOfRemainingDays(dateAsString: String, pattern: String): Long {
    val localDate = convertStringToLocalDate(dateAsString, pattern)
    return convertLocalDateToLong(LocalDate.now()) - convertLocalDateToLong(localDate)
}

fun Long.getNumberOfDays(): Int = (this - convertLocalDateToLong(LocalDate.now())).toInt()

fun Long.toDateAsString(pattern: String): String {
    val localDate = convertLongToLocalDate(this)
    return convertDateToString(localDate, pattern)
}

fun LocalDate.toLong(): Long = this.toEpochDay()
fun LocalDate.toDateAsString(pattern: String): String {
    val formatter = provideFormatter(pattern)
    return this.format(formatter)
}

fun String.toLocalDate(pattern: String): LocalDate {
    val formatter = provideFormatter(pattern)
    return LocalDate.parse(this, formatter)
}

fun transformTasksToTaskStates(tasks: List<Task>): List<TaskState> {
    return tasks.map { task ->
        task.toTaskState()
    }
}
fun transformTaskStatesToTasks(taskStates: List<TaskState>): List<Task> =
    taskStates.map { taskState ->
        taskState.toTask()
    }

fun Task.toTaskState() = TaskState(
    milestoneId = milestoneId,
    taskId = taskId,
    initialTaskTitleState = TaskTextFieldState(
        text = taskTitle
    ),
    initialTaskDescState = TaskTextFieldState(
        text = taskDesc
    ),
    initialStatusState = status,
)

fun TaskState.toTask() = Task(
    milestoneId = milestoneId,
    taskId = taskId,
    taskTitle = taskTitleState.text,
    taskDesc = taskDescState.text,
    status = statusState
)

@Preview(name = "phone", device = Devices.PHONE)
@Preview(name = "foldable", device = Devices.FOLDABLE)
@Preview(name = "custom", device = "spec:width = 1280dp, height = 800dp, dpi=480")
@Preview(name = "desktop", device = "id:desktop_medium")
annotation class ReferenceDevices
