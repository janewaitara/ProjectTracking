package com.mumbicodes.projectie.presentation.screens.allProjects

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.use_case.notifications.NotificationUseCases
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.use_case.workers.WorkersUseCases
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.domain.util.WorkerState
import com.mumbicodes.projectie.presentation.util.state.ListState
import com.mumbicodes.projectie.presentation.util.state.ScreenState
import com.mumbicodes.projectie.presentation.util.state.SuccessState
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

    private val _state: MutableState<ScreenState<AllProjectsStates>> =
        mutableStateOf(ScreenState.Loading)
    val state = _state

    private var recentlyDeletedProject: Project? = null

    private var getProjectsJob: Job? = null

    init {
        getProjects(ProjectsOrder.DateAdded(OrderType.Descending))
        if (state is ScreenState.Data<*>) {
            readNotPromptState()
        }
        checkWorkInfoState()
    }

    fun onEvent(projectsEvent: AllProjectsEvent) {
        when (projectsEvent) {
            is AllProjectsEvent.OrderProjects -> {
                if ((state.value as ScreenState.Data).data.projectsOrder::class == (state.value as ScreenState.Data).data.selectedProjectOrder::class &&
                    (state.value as ScreenState.Data).data.projectsOrder.orderType == (state.value as ScreenState.Data).data.selectedProjectOrder.orderType
                ) {
                    return
                }
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        projectsOrder = (state.value as ScreenState.Data).data.selectedProjectOrder
                    )
                )
            }

            is AllProjectsEvent.ResetProjectsOrder -> {
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        selectedProjectOrder = projectsEvent.projectsOrder
                    )
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
                if ((state.value as ScreenState.Data).data.selectedProjectStatus == projectsEvent.projectStatus) {
                    return
                }
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        selectedProjectStatus = projectsEvent.projectStatus
                    )
                )
            }

            is AllProjectsEvent.ToggleBottomSheetVisibility -> {
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        isFilterBottomSheetVisible = !(state.value as ScreenState.Data<AllProjectsStates>).data.isFilterBottomSheetVisible
                    )
                )
            }

            is AllProjectsEvent.SearchProject -> {
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        searchParam = projectsEvent.searchParam
                    )
                )
            }

            is AllProjectsEvent.UpdateProjectOrder -> {
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data<AllProjectsStates>).data.copy(
                        selectedProjectOrder = projectsEvent.projectsOrder
                    )
                )
            }
        }
    }

    /**
     * Whenever called, a new flow is emitted. Hence cancel the old coroutine that's observing db
     * */
    private fun getProjects(projectsOrder: ProjectsOrder) {
        viewModelScope.launch {

            getProjectsJob?.cancel()
            getProjectsJob =
                when (val results = projectsUseCases.getProjectsUseCase()) {
                    is DataResult.Error -> {
                        // Doing this in a viewModelScope as we need to return a job
                        viewModelScope.launch {
                            _state.value = ScreenState.Data(
                                data = (state.value as ScreenState.Data).data.copy(
                                    projects = ListState.Error(errorMessage = results.errorMessage)
                                )
                            )
                        }
                    }

                    is DataResult.Success -> {
                        results.data
                            // map the flow to AllProjects compose State
                            .onEach { projects ->
                                if (projects.isEmpty()) {
                                    _state.value = ScreenState.Empty
                                } else {
                                    _state.value = ScreenState.Data(
                                        data = AllProjectsStates(
                                            projects = ListState.Success(
                                                data = SuccessState.Data(
                                                    projects
                                                )
                                            ),
                                            projectsOrder = projectsOrder,
                                        ),
                                    )
                                }
                            }
                            .launchIn(viewModelScope)
                    }
                }
        }
    }

    private fun readNotPromptState() {
        viewModelScope.launch {
            notificationUseCases.readNotificationPromptStateUseCase.invoke().collect { isPrompted ->
                _state.value = ScreenState.Data(
                    data = (state.value as ScreenState.Data).data.copy(
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
