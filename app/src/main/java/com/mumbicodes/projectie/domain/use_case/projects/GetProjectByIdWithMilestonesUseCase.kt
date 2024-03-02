package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectByIdWithMilestonesUseCase(
    private val repository: ProjectsRepository,
) {
    suspend operator fun invoke(
        projectId: Int,
    ): DataResult<Flow<ProjectWithMilestones?>> =
        repository.getProjectByIdWithMilestones(projectId)
}
