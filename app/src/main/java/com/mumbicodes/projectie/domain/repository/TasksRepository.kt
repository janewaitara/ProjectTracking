package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.Task

interface TasksRepository {

    suspend fun insertTask(tasks: List<Task>)

    suspend fun deleteTask(task: Task)
}
