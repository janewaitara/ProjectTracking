package com.mumbicodes.presentation.allProjects

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProjectsViewModel @Inject constructor(
    private val projectsUseCases: ProjectsUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(AllProjectsStates())
    val state = _state

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
                getProjects(projectsEvent.projectsOrder, state.value.projectStatus)
            }
            is AllProjectsEvent.ResetProjectsOrder -> {
                _state.value = state.value.copy(
                    projectsOrder = projectsEvent.projectsOrder,
                )
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
                if (state.value.projectStatus == projectsEvent.projectStatus) {
                    return
                }
                getProjects(state.value.projectsOrder, projectsEvent.projectStatus)
            }

            is AllProjectsEvent.ToggleBottomSheetVisibility -> {
                _state.value = state.value.copy(
                    isFilterBottomSheetVisible = !state.value.isFilterBottomSheetVisible
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
            projectsUseCases.getProjectsUseCase(state.value.projectStatus, projectsOrder)
                // map the flow to AllProjects compose State
                .onEach { projects ->
                    _state.value = state.value.copy(
                        projects = projects,
                        projectsOrder = projectsOrder,
                        projectStatus = projectStatus
                    )
                }
                .launchIn(viewModelScope)
    }
}
