package com.mumbicodes.projectie.presentation.screens.allProjects

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.ProjectsOrder

/**
 * These events will be sent from composables to the viewModel and use a when expression in
 * the viewModel to perform corresponding action
 * */
sealed class AllProjectsEvent {
    object OrderProjects : AllProjectsEvent()
    object ToggleBottomSheetVisibility : AllProjectsEvent()
    data class ResetProjectsOrder(val projectsOrder: ProjectsOrder) : AllProjectsEvent()
    data class DeleteProject(val project: Project) : AllProjectsEvent()
    data class RestoreProject(val project: Project) : AllProjectsEvent()
    data class SelectProjectStatus(val projectStatus: String) : AllProjectsEvent()
    data class SearchProject(val searchParam: String) : AllProjectsEvent()
    data class UpdateProjectOrder(val projectsOrder: ProjectsOrder) : AllProjectsEvent()
}
