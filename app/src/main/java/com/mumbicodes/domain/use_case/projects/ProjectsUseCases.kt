package com.mumbicodes.domain.use_case.projects

data class ProjectsUseCases(
    val addProjectsUseCase: AddProjectsUseCase,
    val getProjectByIdUseCase: GetProjectByIdUseCase,
    val getProjectByIdWithMilestonesUseCase: GetProjectByIdWithMilestonesUseCase,
    val getProjectsUseCase: GetProjectsUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase,
    val deleteAllProjectsUseCase: DeleteAllProjectsUseCase,
    val checkProjectStatusUseCase: CheckProjectStatusUseCase,
)
