package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import kotlinx.coroutines.flow.Flow

interface MilestonesRepository {

    suspend fun insertOrUpdateMilestone(milestone: Milestone)

    suspend fun getMilestoneByIdWithTasks(milestoneId: Int): DataResult <Flow<MilestoneWithTasks?>>

    suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): DataResult<List<Milestone>>

    suspend fun getAllMilestones(): DataResult<Flow<List<MilestoneWithTasks>>>

    suspend fun deleteMilestone(milestone: Milestone)

    suspend fun deleteMilestonesForProject(projectId: Int)

    suspend fun deleteAllMilestones()
}
