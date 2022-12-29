package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.repository.MilestonesRepository

class GetMilestonesUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        status: String,
    ): List<Milestone> =
        repository.getAllMilestonesBasedOnProjIdAndStatus(projectId, status)
}
