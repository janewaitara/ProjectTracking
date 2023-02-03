package com.mumbicodes.projectie.domain.use_case.tasks

import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.repository.TasksRepository

class DeleteTaskUseCase(
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}
