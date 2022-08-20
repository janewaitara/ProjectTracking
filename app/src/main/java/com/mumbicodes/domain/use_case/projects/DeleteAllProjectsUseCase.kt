package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.repository.ProjectsRepository

class DeleteAllProjectsUseCase(
    private val repository: ProjectsRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAllProjects()
    }
}
