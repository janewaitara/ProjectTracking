package com.mumbicodes.presentation.allProjects

import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder

data class AllProjectsStates(
    val projects: List<Project> = emptyList(),
    val projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    val projectStatus: String = "All",
    val isFilterBottomSheetVisible: Boolean = false,
)
