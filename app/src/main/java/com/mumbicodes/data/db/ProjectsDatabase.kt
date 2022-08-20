package com.mumbicodes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project

@Database(entities = [Project::class, Milestone::class], version = 1, exportSchema = false)
@TypeConverters(TaskConverter::class)
abstract class ProjectsDatabase : RoomDatabase() {

    abstract fun projectsDao(): ProjectsDao
    abstract fun milestonesDao(): MilestonesDao
}
