package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.Task

interface TasksRepository {

    suspend fun insertOrUpdateTask(tasks: List<Task>)

    suspend fun deleteTask(task: Task)
}
