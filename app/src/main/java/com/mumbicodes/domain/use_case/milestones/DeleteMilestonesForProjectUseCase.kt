package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.repository.MilestonesRepository

class DeleteMilestonesForProjectUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(projectId: Int) {
        repository.deleteMilestonesForProject(projectId)
    }
}
