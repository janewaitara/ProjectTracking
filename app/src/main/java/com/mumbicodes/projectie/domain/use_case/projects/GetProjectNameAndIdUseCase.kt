package com.mumbicodes.projectie.domain.use_case.projects

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectNameAndIdUseCase(
    private val repository: ProjectsRepository,
) {
    suspend operator fun invoke(): DataResult<Flow<List<ProjectName>>> =
        repository.getProjectNameAndId()
}
