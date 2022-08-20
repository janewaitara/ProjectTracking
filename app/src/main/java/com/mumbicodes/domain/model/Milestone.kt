package com.mumbicodes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestones_table")
data class Milestone(
    @PrimaryKey(autoGenerate = true) val projectId: String,
    val milestoneId: String,
    val milestoneTitle: String,
    val milestoneSrtDate: Long,
    val milestoneEndDate: Long,
    val status: String,
    val tasks: List<Task>,
)
