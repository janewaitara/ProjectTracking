package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.repository.MilestonesRepository

class GetMilestonesUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        status: String,
    ): DataResult<List<Milestone>> =
        repository.getAllMilestonesBasedOnProjIdAndStatus(projectId, status)
}
