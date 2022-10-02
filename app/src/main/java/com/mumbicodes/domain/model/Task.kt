package com.mumbicodes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
    val milestoneId: Int,
    @PrimaryKey(autoGenerate = false)
    val taskId: Int,
    val taskTitle: String,
    val taskDesc: String,
    val status: Boolean,
)
