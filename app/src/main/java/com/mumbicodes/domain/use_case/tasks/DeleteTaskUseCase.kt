package com.mumbicodes.domain.use_case.tasks

import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.repository.TasksRepository

class DeleteTaskUseCase(
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}
