package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.relations.ProjectWithMilestones
import com.mumbicodes.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectByIdWithMilestonesUseCase(
    private val repository: ProjectsRepository,
) {
    operator fun invoke(
        projectId: Int,
    ): Flow<ProjectWithMilestones?> =
        repository.getProjectByIdWithMilestones(projectId)
}
