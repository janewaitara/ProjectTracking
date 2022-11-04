package com.mumbicodes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.model.Task

@Database(entities = [Project::class, Milestone::class, Task::class], version = 3, exportSchema = false)
@TypeConverters(TaskConverter::class)
abstract class ProjectsDatabase : RoomDatabase() {

    abstract fun projectsDao(): ProjectsDao
    abstract fun milestonesDao(): MilestonesDao
    abstract fun tasksDao(): TasksDao
}
