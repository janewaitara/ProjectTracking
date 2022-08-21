package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val repository: ProjectsRepository
) {
    suspend operator fun invoke(projectId: Int) {
        repository.getProjectById(projectId)
    }
}
