package com.mumbicodes.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "milestones_table",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["projectId"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
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
