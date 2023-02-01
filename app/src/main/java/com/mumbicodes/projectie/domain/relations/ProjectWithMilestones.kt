package com.mumbicodes.projectie.domain.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Project

data class ProjectWithMilestones(
    @Embedded val project: Project,
    @Relation(
        entity = Milestone::class,
        parentColumn = "projectId",
        entityColumn = "projectId"
    )
    val milestones: List<MilestoneWithTasks>
)
