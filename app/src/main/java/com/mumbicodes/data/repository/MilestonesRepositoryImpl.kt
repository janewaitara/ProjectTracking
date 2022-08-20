package com.mumbicodes.data.repository

import com.mumbicodes.data.db.MilestonesDao
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class MilestonesRepositoryImpl(
    private val milestonesDao: MilestonesDao,
) : MilestonesRepository {

    override suspend fun insertMilestone(milestone: Milestone) {
        milestonesDao.insertMilestone(milestone)
    }

    override suspend fun getMilestoneById(milestoneId: String): Milestone =
        milestonesDao.getMilestoneById(milestoneId)

    override fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): Flow<List<Milestone>> =
        milestonesDao.getAllMilestonesBasedOnProjIdAndStatus(projectId, status)

    override suspend fun deleteMilestone(milestone: Milestone) {
        milestonesDao.deleteMilestone(milestone)
    }

    override suspend fun deleteMilestonesForProject(projectId: Int) {
        milestonesDao.deleteMilestonesForProject(projectId)
    }

    override suspend fun deleteAllMilestones() {
        milestonesDao.deleteAllMilestones()
    }
}
