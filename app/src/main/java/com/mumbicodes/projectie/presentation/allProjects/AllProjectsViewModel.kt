package com.mumbicodes.projectie.presentation.allProjects

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.use_case.notifications.NotificationUseCases
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.use_case.workers.WorkersUseCases
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.domain.util.WorkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProjectsViewModel @Inject constructor(
    private val projectsUseCases: ProjectsUseCases,
    private val notificationUseCases: NotificationUseCases,
    private val workersUserUseCases: WorkersUseCases,
    private val appContext: Application,
) : ViewModel() {

    private val _state = mutableStateOf(AllProjectsScreenStates())
    val state = _state

    private val _searchParam = mutableStateOf("")
    val searchParam = _searchParam

    private var recentlyDeletedProject: Project? = null

    private var getProjectsJob: Job? = null

    init {
        getProjects(ProjectsOrder.DateAdded(OrderType.Descending), "All")
        readNotPromptState()
        checkWorkInfoState()
    }

    fun onEvent(projectsEvent: AllProjectsEvent) {
        when (projectsEvent) {
            is AllProjectsEvent.OrderProjects -> {
                if (state.value.data.projectsOrder::class == projectsEvent.projectsOrder::class &&
                    state.value.data.projectsOrder.orderType == projectsEvent.projectsOrder.orderType
                ) {
                    return
                }
                getProjects(projectsEvent.projectsOrder, state.value.data.selectedProjectStatus)
            }

            is AllProjectsEvent.ResetProjectsOrder -> {

                getProjects(projectsEvent.projectsOrder, state.value.data.selectedProjectStatus)
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
                if (state.value.data.selectedProjectStatus == projectsEvent.projectStatus) {
                    return
                }
                state.value.data.projects.filterProjects(
                    projectStatus = projectsEvent.projectStatus,
                    searchParam = searchParam.value
                )
            }

            is AllProjectsEvent.ToggleBottomSheetVisibility -> {
                _state.value = state.value.copy(
                    data = state.value.data.copy(
                        isFilterBottomSheetVisible = !state.value.data.isFilterBottomSheetVisible
                    )
                )
            }

            is AllProjectsEvent.SearchProject -> {
                _searchParam.value = projectsEvent.searchParam

                state.value.data.projects.filterProjects(
                    projectStatus = state.value.data.selectedProjectStatus,
                    searchParam = projectsEvent.searchParam,
                )
            }
        }
    }

    /**
     * Whenever called, a new flow is emitted. Hence cancel the old coroutine that's observing db
     * */
    private fun getProjects(projectsOrder: ProjectsOrder, projectStatus: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )

            getProjectsJob?.cancel()
            getProjectsJob =
                projectsUseCases.getProjectsUseCase(projectsOrder)
                    // map the flow to AllProjects compose State
                    .onEach { projects ->
                        _state.value = state.value.copy(
                            data = state.value.data.copy(
                                projects = projects,
                                projectsOrder = projectsOrder,
                            ),
                            isLoading = false
                        )
                        projects.filterProjects(projectStatus, searchParam.value)
                    }
                    .launchIn(viewModelScope)
        }
    }

    private fun List<Project>.filterProjects(
        projectStatus: String,
        searchParam: String,
    ) {
        _state.value = state.value.copy(
            data = state.value.data.copy(
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
                selectedProjectStatus = projectStatus,
            ),
            isLoading = false
        )
    }

    private fun readNotPromptState() {
        viewModelScope.launch {
            notificationUseCases.readNotificationPromptStateUseCase.invoke().collect { isPrompted ->
                _state.value = state.value.copy(
                    data = state.value.data.copy(
                        hasRequestedNotificationPermission = isPrompted
                    )
                )
                Log.e("Notification Permission", isPrompted.toString())
            }
        }
    }

    fun saveNotPromptState(isPrompted: Boolean) {
        viewModelScope.launch {
            notificationUseCases.saveNotificationPromptStateUseCase(isPrompted)
        }
    }

    /**
     * For users that had installed the app before the worker was enqueued on the onBoarding screen
     * The worker will be build from here
     * */
    private fun checkWorkInfoState() {
        viewModelScope.launch {
            workersUserUseCases.checkWorkInfoStateUseCase.invoke()
                .collectLatest { projectsWorkerState ->
                    when (projectsWorkerState) {
                        WorkerState.STARTED -> {} // do nothing
                        WorkerState.NOT_STARTED -> workersUserUseCases.checkDeadlinesUseCase.invoke()
                    }
                }
        }
    }
}
