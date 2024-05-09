package com.mumbicodes.projectie.presentation.screens.allProjects

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.presentation.util.state.ListState
import com.mumbicodes.projectie.presentation.util.state.SuccessState

/**
 * @param projectsOrder This is the actual projects order after the user updates the filters
 * @param selectedProjectOrder Holds the user selection until they press filter
 * */
data class AllProjectsStates(
    val projects: ListState<List<Project>> = ListState.Loading,
    val projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    val filtersStatus: List<String> = filters,
    val selectedProjectStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
    val hasRequestedNotificationPermission: Boolean = false,
    val selectedProjectOrder: ProjectsOrder = projectsOrder,
    val searchParam: String = "",
) {

    val filteredProjects: ListState<List<Project>>
        get() = if (projects is ListState.Success) {
            if (projects.data is SuccessState.Data) {
                val allProjects = projects.data.data
                val filteredList = allProjects
                    .filter {
                        if (selectedProjectStatus == filters.first()) true
                        else selectedProjectStatus == it.projectStatus
                    }
                    .filter {
                        if (searchParam.isNotBlank()) it.projectName.contains(searchParam)
                        else true
                    }
                    .let { projectsList ->
                        when (selectedProjectOrder.orderType) {
                            is OrderType.Ascending -> {
                                when (selectedProjectOrder) {
                                    is ProjectsOrder.Name -> projectsList.sortedBy { it.projectName.lowercase() }
                                    is ProjectsOrder.Deadline -> projectsList.sortedBy { it.projectDeadline }
                                    is ProjectsOrder.DateAdded -> projectsList.sortedBy { it.timeStamp }
                                }
                            }

                            is OrderType.Descending -> {
                                when (selectedProjectOrder) {
                                    is ProjectsOrder.Name -> projectsList.sortedByDescending { it.projectName.lowercase() }
                                    is ProjectsOrder.Deadline -> projectsList.sortedByDescending { it.projectDeadline }
                                    is ProjectsOrder.DateAdded -> projectsList.sortedByDescending { it.timeStamp }
                                }
                            }
                        }
                    }

                if (filteredList.isEmpty()) ListState.Success(SuccessState.Empty)
                else ListState.Success(SuccessState.Data(data = filteredList))
            } else {
                projects
            }
        } else {
            projects
        }
}

val filters = listOf(
    "All",
    "Not Started",
    "In Progress",
    "Completed",
)
