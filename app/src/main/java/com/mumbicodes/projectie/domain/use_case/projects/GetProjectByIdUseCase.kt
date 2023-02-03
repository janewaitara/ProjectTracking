package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val repository: ProjectsRepository
) {
    suspend operator fun invoke(projectId: Int): Project =
        repository.getProjectById(projectId)
}
