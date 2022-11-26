package com.mumbicodes.domain.repository

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.relations.MilestoneWithTasks
import kotlinx.coroutines.flow.Flow

interface MilestonesRepository {

    suspend fun insertMilestone(milestone: Milestone)

    fun getMilestoneByIdWithTasks(milestoneId: Int): Flow<MilestoneWithTasks?>

    suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): List<Milestone>

    suspend fun deleteMilestone(milestone: Milestone)

    suspend fun deleteMilestonesForProject(projectId: Int)

    suspend fun deleteAllMilestones()
}
