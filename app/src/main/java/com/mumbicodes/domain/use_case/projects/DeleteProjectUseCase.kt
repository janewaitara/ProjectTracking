package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.repository.ProjectsRepository

class DeleteProjectUseCase(
    private val repository: ProjectsRepository,
) {
    suspend operator fun invoke(project: Project) {
        repository.deleteProject(project)
    }
}
