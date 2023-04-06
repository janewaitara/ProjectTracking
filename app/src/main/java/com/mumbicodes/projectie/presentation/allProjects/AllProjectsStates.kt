package com.mumbicodes.projectie.presentation.allProjects

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder

data class AllProjectsStates(
    val projects: List<Project> = emptyList(),
    val filteredProjects: List<Project> = emptyList(),
    val projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    val filtersStatus: List<String> = filters,
    val selectedProjectStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
    val hasRequestedNotificationPermission: Boolean = false,
)

val filters = listOf(
    "All",
    "Not Started",
    "In Progress",
    "Completed",
)

data class AllProjectsScreenStates(
    val isLoading: Boolean = false,
    val data: AllProjectsStates = AllProjectsStates(),
)
