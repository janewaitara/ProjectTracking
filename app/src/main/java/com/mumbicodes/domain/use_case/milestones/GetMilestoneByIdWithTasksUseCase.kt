package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.relations.MilestoneWithTasks
import com.mumbicodes.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class GetMilestoneByIdWithTasksUseCase(
    private val repository: MilestonesRepository
) {
    operator fun invoke(milestoneId: Int): Flow<MilestoneWithTasks?> =
        repository.getMilestoneByIdWithTasks(milestoneId)
}
