package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.repository.MilestonesRepository

class DeleteAllMilestonesUseCase(
    private val repository: MilestonesRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAllMilestones()
    }
}
