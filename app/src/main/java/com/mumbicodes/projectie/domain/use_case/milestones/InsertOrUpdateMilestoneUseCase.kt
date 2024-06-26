package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.repository.MilestonesRepository

class InsertOrUpdateMilestoneUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(milestone: Milestone) {
        repository.insertOrUpdateMilestone(milestone)
    }
}
