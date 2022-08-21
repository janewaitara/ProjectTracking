package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.repository.MilestonesRepository

class GetMilestoneByIdUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(milestoneId: Int): Milestone =
        repository.getMilestoneById(milestoneId)
}
