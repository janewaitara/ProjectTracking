package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.repository.MilestonesRepository

class DeleteMilestonesForProjectUseCase(
    private val repository: MilestonesRepository
) {
    suspend operator fun invoke(projectId: Int) {
        repository.deleteMilestonesForProject(projectId)
    }
}
