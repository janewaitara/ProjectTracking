package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class GetMilestonesUseCase(
    private val repository: MilestonesRepository
) {
    operator fun invoke(
        projectId: Int,
        status: String,
    ): Flow<List<Milestone>> =
        repository.getAllMilestonesBasedOnProjIdAndStatus(projectId, status)
}
