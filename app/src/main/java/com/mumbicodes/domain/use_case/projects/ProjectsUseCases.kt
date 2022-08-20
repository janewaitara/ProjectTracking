package com.mumbicodes.domain.use_case.projects

data class ProjectsUseCases(
    val addProjectsUseCase: AddProjectsUseCase,
    val getProjectByIdUseCase: GetProjectByIdUseCase,
    val getProjectsUseCase: GetProjectUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase,
    val deleteAllProjectsUseCase: DeleteAllProjectsUseCase,
)
