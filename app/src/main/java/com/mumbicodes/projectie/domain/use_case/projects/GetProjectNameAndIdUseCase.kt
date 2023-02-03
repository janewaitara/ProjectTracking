package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectNameAndIdUseCase(
    private val repository: ProjectsRepository,
) {
    operator fun invoke(): Flow<List<ProjectName>> =
        repository.getProjectNameAndId()
}
