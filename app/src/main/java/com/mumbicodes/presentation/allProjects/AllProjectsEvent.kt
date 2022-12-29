package com.mumbicodes.presentation.allProjects

import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.util.ProjectsOrder

/**
 * These events will be sent from composables to the viewModel and use a when expression in
 * the viewModel to perform corresponding action
 * */
sealed class AllProjectsEvent {
    data class OrderProjects(val projectsOrder: ProjectsOrder) : AllProjectsEvent()
    data class ResetProjectsOrder(val projectsOrder: ProjectsOrder) : AllProjectsEvent()
    data class DeleteProject(val project: Project) : AllProjectsEvent()
    data class RestoreProject(val project: Project) : AllProjectsEvent()
    data class SelectProjectStatus(val projectStatus: String) : AllProjectsEvent()
    data class SearchProject(val searchParam: String) : AllProjectsEvent()
    object ToggleBottomSheetVisibility : AllProjectsEvent()
}
