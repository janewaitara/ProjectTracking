package com.mumbicodes.projectie.data.repository

import com.mumbicodes.projectie.data.db.TasksDao
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.repository.TasksRepository

class TaskRepositoryImpl(private val tasksDao: TasksDao) : TasksRepository {
    override suspend fun insertTask(tasks: List<Task>) {
        tasksDao.insertTask(tasks)
    }

    override suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }
}
