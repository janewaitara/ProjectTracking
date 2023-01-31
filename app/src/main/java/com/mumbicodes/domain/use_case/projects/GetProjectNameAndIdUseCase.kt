package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.model.ProjectName
import com.mumbicodes.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectNameAndIdUseCase(
    private val repository: ProjectsRepository,
) {
    operator fun invoke(): Flow<List<ProjectName>> =
        repository.getProjectNameAndId()
}
