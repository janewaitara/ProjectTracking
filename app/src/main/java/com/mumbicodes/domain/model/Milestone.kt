package com.mumbicodes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestones_table")
data class Milestone(
    val projectId: Int,
    @PrimaryKey(autoGenerate = true)
    val milestoneId: Int = 0,
    val milestoneTitle: String,
    val milestoneSrtDate: Long,
    val milestoneEndDate: Long,
    val status: String,
    val tasks: List<Task>,
)
