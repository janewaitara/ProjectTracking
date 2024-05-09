package com.mumbicodes.projectie.domain.use_case.tasks

import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.repository.TasksRepository

class InsertOrUpdateTasksUseCase(
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(tasks: List<Task>) {
        repository.insertOrUpdateTask(tasks)
    }
}
