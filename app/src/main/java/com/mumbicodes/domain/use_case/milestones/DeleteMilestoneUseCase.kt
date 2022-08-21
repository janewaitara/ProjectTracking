package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.repository.MilestonesRepository

class DeleteMilestoneUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(milestone: Milestone) {
        repository.deleteMilestone(milestone)
    }
}
