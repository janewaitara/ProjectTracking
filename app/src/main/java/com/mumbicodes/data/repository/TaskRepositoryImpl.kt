package com.mumbicodes.data.repository

import com.mumbicodes.data.db.TasksDao
import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.repository.TasksRepository

class TaskRepositoryImpl(private val tasksDao: TasksDao) : TasksRepository {
    override suspend fun insertTask(tasks: List<Task>) {
        tasksDao.insertTask(tasks)
    }

    override suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }
}
