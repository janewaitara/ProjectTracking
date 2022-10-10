package com.mumbicodes.domain.repository

import com.mumbicodes.domain.model.Task

interface TasksRepository {

    suspend fun insertTask(tasks: List<Task>)

    suspend fun deleteTask(task: Task)
}
