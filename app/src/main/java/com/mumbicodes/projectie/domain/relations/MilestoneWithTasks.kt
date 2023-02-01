package com.mumbicodes.projectie.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Task

data class MilestoneWithTasks(
    @Embedded
    val milestone: Milestone,
    @Relation(
        parentColumn = "milestoneId",
        entityColumn = "milestoneId"
    )
    val tasks: List<Task>,
)
