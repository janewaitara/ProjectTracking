package com.mumbicodes.presentation.allProjects

import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder

data class AllProjectsStates(
    val projects: List<Project> = emptyList(),
    val filteredProjects: List<Project> = emptyList(),
    val projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    val filtersStatus: List<String> = filters,
    val selectedProjectStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
)

val filters = listOf(
    "All",
    "Not Started",
    "In Progress",
    "Completed",
)
