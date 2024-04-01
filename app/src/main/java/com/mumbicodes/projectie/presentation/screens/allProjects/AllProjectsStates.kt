package com.mumbicodes.projectie.presentation.screens.allProjects

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.presentation.util.state.ListState

/**
 * @param selectedProjectOrder  Holds the user selection until they press filter
 *
 * There is the empty state when there is nothing created -- Screen Empty -- the db is empty
 * There is loading of the screen -- Initial state
 * There is data -- Screen data
 *
 * Projects - Error, Loading, Success (Empty, Data)
 *      The overall data
 *      The search results -- within the same data
 *
 *
 * - projects - The projects we are showing (default and search results)-- All projects - have the states
 * - filters - name and selected state @param filtersStatus,
 * @param filtersStatus and @param selectedProjectStatus
 *
 * - Projects Order
 * - showFilterBottomSheet
 * - has requested Notification
 * - selected project order
 * - Search param
 * -
 *
 * */
data class AllProjectsStates(
    val projects: ListState<List<Project>> = ListState.Loading,
    val filteredProjects: ListState<List<Project>> = ListState.Loading,
    val projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    val filtersStatus: List<String> = filters,
    val selectedProjectStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
    val hasRequestedNotificationPermission: Boolean = false,
    val selectedProjectOrder: ProjectsOrder = projectsOrder,
    val searchParam: String = "",
)

val filters = listOf(
    "All",
    "Not Started",
    "In Progress",
    "Completed",
)
