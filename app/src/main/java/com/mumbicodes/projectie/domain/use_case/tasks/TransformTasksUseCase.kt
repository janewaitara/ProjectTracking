package com.mumbicodes.projectie.domain.use_case.tasks

import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.presentation.screens.add_edit_milestone.TaskState
import com.mumbicodes.projectie.presentation.screens.add_edit_milestone.TaskTextFieldState

class TransformTasksUseCase {
    fun transformTasksToTaskStates(tasks: List<Task>): List<TaskState> {
        return tasks.map { task ->
            task.toTaskState()
        }
    }
    fun transformTaskStatesToTasks(taskStates: List<TaskState>): List<Task> =
        taskStates.map { taskState ->
            taskState.toTask()
        }

    private fun Task.toTaskState() = TaskState(
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
    private fun TaskState.toTask() = Task(
        milestoneId = milestoneId,
        taskId = taskId,
        taskTitle = taskTitleState.text,
        taskDesc = taskDescState.text,
        status = statusState
    )
}
