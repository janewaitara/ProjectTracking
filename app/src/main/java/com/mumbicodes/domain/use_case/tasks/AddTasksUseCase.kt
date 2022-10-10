package com.mumbicodes.domain.use_case.tasks

import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.repository.TasksRepository

class AddTasksUseCase(
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(tasks: List<Task>) {
        repository.insertTask(tasks)
    }
}
