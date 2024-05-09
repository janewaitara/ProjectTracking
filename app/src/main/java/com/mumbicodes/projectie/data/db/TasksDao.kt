package com.mumbicodes.projectie.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.mumbicodes.projectie.domain.model.Task

@Dao
interface TasksDao {

    @Upsert
    suspend fun insertOrUpdateTask(tasks: List<Task>)

    @Delete
    suspend fun deleteTask(task: Task)
}
