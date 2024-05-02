package com.mumbicodes.projectie.data.repository

import com.mumbicodes.projectie.data.db.MilestonesDao
import com.mumbicodes.projectie.data.helpers.safeTransaction
import com.mumbicodes.projectie.data.helpers.toDataResult
import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class MilestonesRepositoryImpl(
    private val milestonesDao: MilestonesDao,
) : MilestonesRepository {

    override suspend fun insertMilestone(milestone: Milestone) {
        milestonesDao.insertMilestone(milestone)
    }

    override suspend fun getMilestoneByIdWithTasks(milestoneId: Int): DataResult<Flow<MilestoneWithTasks?>> =
        safeTransaction {
            milestonesDao.getMilestoneByIdWithTasks(milestoneId)
        }.toDataResult()

    override suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): DataResult<List<Milestone>> = safeTransaction {
        milestonesDao.getAllMilestonesBasedOnProjIdAndStatus(projectId)
    }.toDataResult()

    override suspend fun getAllMilestones(): DataResult<Flow<List<MilestoneWithTasks>>> =
        safeTransaction { milestonesDao.getAllMilestones() }.toDataResult()

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
