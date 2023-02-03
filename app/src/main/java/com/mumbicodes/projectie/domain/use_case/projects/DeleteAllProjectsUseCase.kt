package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.repository.ProjectsRepository

class DeleteAllProjectsUseCase(
    private val repository: ProjectsRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllProjects()
    }
}
