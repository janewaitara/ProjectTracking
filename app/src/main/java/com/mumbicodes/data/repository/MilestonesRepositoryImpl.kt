package com.mumbicodes.data.repository

import com.mumbicodes.data.db.MilestonesDao
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.relations.MilestoneWithTasks
import com.mumbicodes.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class MilestonesRepositoryImpl(
    private val milestonesDao: MilestonesDao,
) : MilestonesRepository {

    override suspend fun insertMilestone(milestone: Milestone) {
        milestonesDao.insertMilestone(milestone)
    }

    override fun getMilestoneByIdWithTasks(milestoneId: Int): Flow<MilestoneWithTasks?> =
        milestonesDao.getMilestoneByIdWithTasks(milestoneId)

    override suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): List<Milestone> =
        milestonesDao.getAllMilestonesBasedOnProjIdAndStatus(projectId)

    override fun getAllMilestones(): Flow<List<MilestoneWithTasks>> =
        milestonesDao.getAllMilestones()

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
