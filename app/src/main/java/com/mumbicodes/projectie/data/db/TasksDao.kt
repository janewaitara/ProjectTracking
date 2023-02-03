package com.mumbicodes.projectie.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mumbicodes.projectie.domain.model.Task

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: List<Task>)

    @Delete
    suspend fun deleteTask(task: Task)
}
