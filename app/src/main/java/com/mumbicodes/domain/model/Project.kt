package com.mumbicodes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects_table")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val projectId: Int = 0,
    val projectName: String,
    val projectDesc: String,
    val projectDeadline: String,
    val projectStatus: String,
    val timeStamp: Long
)
