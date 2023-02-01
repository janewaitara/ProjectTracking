package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.repository.MilestonesRepository

class DeleteAllMilestonesUseCase(
    private val repository: MilestonesRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAllMilestones()
    }
}
