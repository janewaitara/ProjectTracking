package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class GetMilestoneByIdWithTasksUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(milestoneId: Int): DataResult<Flow<MilestoneWithTasks?>> =
        repository.getMilestoneByIdWithTasks(milestoneId)
}
