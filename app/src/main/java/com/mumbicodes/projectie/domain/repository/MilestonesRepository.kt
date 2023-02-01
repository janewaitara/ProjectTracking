package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import kotlinx.coroutines.flow.Flow

interface MilestonesRepository {

    suspend fun insertMilestone(milestone: Milestone)

    fun getMilestoneByIdWithTasks(milestoneId: Int): Flow<MilestoneWithTasks?>

    suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): List<Milestone>

    fun getAllMilestones(): Flow<List<MilestoneWithTasks>>

    suspend fun deleteMilestone(milestone: Milestone)

    suspend fun deleteMilestonesForProject(projectId: Int)

    suspend fun deleteAllMilestones()
}
