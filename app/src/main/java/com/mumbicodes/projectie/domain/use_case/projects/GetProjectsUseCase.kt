package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectsUseCase(
    private val repository: ProjectsRepository,
) {
    suspend operator fun invoke(): DataResult<Flow<List<Project>>> =
        repository.getAllProjects()
}
