package com.mumbicodes.projectie.presentation.allProjects

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProjectsViewModel @Inject constructor(
    private val projectsUseCases: ProjectsUseCases,
    private val appContext: Application,
) : ViewModel() {

    private val _state = mutableStateOf(AllProjectsStates())
    val state = _state

    private val _searchParam = mutableStateOf("")
    val searchParam = _searchParam

    private var recentlyDeletedProject: Project? = null

    private var getProjectsJob: Job? = null

    init {
        getProjects(ProjectsOrder.DateAdded(OrderType.Descending), "All")
    }

    fun onEvent(projectsEvent: AllProjectsEvent) {
        when (projectsEvent) {
            is AllProjectsEvent.OrderProjects -> {
                if (state.value.projectsOrder::class == projectsEvent.projectsOrder::class &&
                    state.value.projectsOrder.orderType == projectsEvent.projectsOrder.orderType
                ) {
                    return
                }
                getProjects(projectsEvent.projectsOrder, state.value.selectedProjectStatus)
            }
            is AllProjectsEvent.ResetProjectsOrder -> {

                getProjects(projectsEvent.projectsOrder, state.value.selectedProjectStatus)
            }
            is AllProjectsEvent.DeleteProject -> {
                viewModelScope.launch {
                    projectsUseCases.deleteProjectUseCase(projectsEvent.project)
                    recentlyDeletedProject = projectsEvent.project
                }
            }
            is AllProjectsEvent.RestoreProject -> {
                viewModelScope.launch {
                    projectsUseCases.addProjectsUseCase(recentlyDeletedProject ?: return@launch)
                    recentlyDeletedProject = null
                }
            }
            is AllProjectsEvent.SelectProjectStatus -> {
                if (state.value.selectedProjectStatus == projectsEvent.projectStatus) {
                    return
                }
                state.value.projects.filterProjects(
                    projectStatus = projectsEvent.projectStatus,
                    searchParam = searchParam.value
                )
            }

            is AllProjectsEvent.ToggleBottomSheetVisibility -> {
                _state.value = state.value.copy(
                    isFilterBottomSheetVisible = !state.value.isFilterBottomSheetVisible
                )
            }
            is AllProjectsEvent.SearchProject -> {
                _searchParam.value = projectsEvent.searchParam

                state.value.projects.filterProjects(
                    projectStatus = state.value.selectedProjectStatus,
                    searchParam = projectsEvent.searchParam,
                )
            }
        }
    }

    /**
     * Whenever called, a new flow is emitted. Hence cancel the old coroutine that's observing db
     * */
    private fun getProjects(projectsOrder: ProjectsOrder, projectStatus: String) {
        getProjectsJob?.cancel()
        getProjectsJob =
            projectsUseCases.getProjectsUseCase(projectsOrder)
                // map the flow to AllProjects compose State
                .onEach { projects ->
                    _state.value = state.value.copy(
                        projects = projects,
                        projectsOrder = projectsOrder,
                    )
                    projects.filterProjects(projectStatus, searchParam.value)
                }
                .launchIn(viewModelScope)
    }

    private fun List<Project>.filterProjects(
        projectStatus: String,
        searchParam: String,
    ) {
        _state.value = state.value.copy(
            filteredProjects = if (projectStatus == appContext.getString(R.string.all)) {
                this.filter {
                    it.projectName.contains(searchParam)
                }
            } else {
                this.filter {
                    it.projectStatus == projectStatus
                }.filter {
                    it.projectName.contains(searchParam)
                }
            },
            selectedProjectStatus = projectStatus
        )
    }
}
