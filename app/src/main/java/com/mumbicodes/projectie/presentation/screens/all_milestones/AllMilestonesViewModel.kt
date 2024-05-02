package com.mumbicodes.projectie.presentation.screens.all_milestones

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.use_case.tasks.TasksUseCases
import com.mumbicodes.projectie.presentation.screens.add_edit_milestone.TaskState
import com.mumbicodes.projectie.presentation.util.state.ListState
import com.mumbicodes.projectie.presentation.util.state.ScreenState
import com.mumbicodes.projectie.presentation.util.state.SuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMilestonesViewModel @Inject constructor(
    private val milestonesUseCases: MilestonesUseCases,
    private val projectsUseCases: ProjectsUseCases,
    private val tasksUseCase: TasksUseCases,
) : ViewModel() {

    private val _screenStates: MutableState<ScreenState<AllMilestonesStates>> =
        mutableStateOf(ScreenState.Loading)
    val screenStates = _screenStates

    private var getMilestonesJob: Job? = null
    private var getProjectsJob: Job? = null
    private var getAllMilestonesJob: Job? = null

    private val _uiEvents = MutableSharedFlow<AllMilestonesUIEvents>()
    val uiEvents = _uiEvents

    private val _projectNames: MutableState<List<ProjectName>> = mutableStateOf(emptyList())

    private var _stateTasks = mutableListOf<TaskState>().toMutableStateList()
    val stateTasks: List<TaskState> = _stateTasks

    init {
        getAllMilestones()
    }

    /**
     * why am I reassigning milestone?
     * when the milestone changes the flow emits again and it automatically set to null,
     * so we set it to the previous selected milestone so that it doesn't disappear
     */
    private fun getAllMilestones() {
        viewModelScope.launch {
            getMilestonesJob?.cancel()

            getMilestonesJob = when (
                val allMilestones =
                    milestonesUseCases.getAllMilestonesUseCase()
            ) {
                is DataResult.Error -> TODO()
                is DataResult.Success -> {
                    allMilestones.data.onEach { milestonesWithTasks ->
                        _screenStates.value = ScreenState.Data(
                            data = AllMilestonesStates(
                                mileStone = getNullableSuccessScreenState()?.data?.mileStone,
                                milestones = ListState.Success(
                                    data = SuccessState.Data(
                                        data = milestonesWithTasks,
                                    )
                                )
                            )
                        )
                        getProjectNameAndId()
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private fun getProjectNameAndId() {
        viewModelScope.launch {
            getProjectsJob?.cancel()
            getProjectsJob = when (val results = projectsUseCases.getProjectNameAndIdUseCase()) {

                is DataResult.Error -> TODO()
                is DataResult.Success -> {
                    results.data.onEach { projectNames ->
                        _projectNames.value = projectNames

                        mapProjectNameWithMilestoneId()
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private fun mapProjectNameWithMilestoneId() {

        val mappedMilestonesWithProjectName = mutableMapOf<Int, String>()

        _projectNames.value.forEach { projectName ->
            (((screenStates.value as ScreenState.Data).data.milestones as ListState.Success).data as SuccessState.Data).data.forEach { milestoneWithTasks ->
                if (milestoneWithTasks.milestone.projectId == projectName.projectId) {
                    mappedMilestonesWithProjectName[milestoneWithTasks.milestone.milestoneId] =
                        projectName.projectName
                }
            }
        }
        _screenStates.value = ScreenState.Data(
            data = (screenStates.value as ScreenState.Data<AllMilestonesStates>).data.copy(
                milestonesProjectName = mappedMilestonesWithProjectName
            )
        )
    }

    fun onEvent(milestonesEvents: AllMilestonesEvents) {
        when (milestonesEvents) {
            is AllMilestonesEvents.DeleteMilestone -> {
                viewModelScope.launch {
                    milestonesUseCases.deleteMilestoneUseCase(milestonesEvents.milestone)

                    uiEvents.emit(AllMilestonesUIEvents.DeleteMilestone)
                }
            }

            is AllMilestonesEvents.OrderMilestones -> {
                if ((screenStates.value as ScreenState.Data).data.milestonesOrder::class == (screenStates.value as ScreenState.Data).data.selectedMilestoneOrder::class) {
                    return
                }
                _screenStates.value = getSuccessScreenState().copy(
                    data = getSuccessScreenState().data.copy(
                        milestonesOrder = getSuccessScreenState().data.selectedMilestoneOrder
                    )
                )
            }

            is AllMilestonesEvents.UpdateMilestoneOrder -> {
                _screenStates.value = getSuccessScreenState().copy(
                    data = getSuccessScreenState().data.copy(
                        selectedMilestoneOrder = milestonesEvents.milestonesOrder
                    )
                )
            }

            is AllMilestonesEvents.ResetMilestonesOrder -> {
                _screenStates.value = getSuccessScreenState().copy(
                    data = getSuccessScreenState().data.copy(
                        selectedMilestoneOrder = milestonesEvents.milestonesOrder,
                        milestonesOrder = milestonesEvents.milestonesOrder
                    )
                )
            }

            is AllMilestonesEvents.SearchMilestone -> {
                _screenStates.value = getSuccessScreenState().copy(
                    data = getSuccessScreenState().data.copy(
                        searchParam = milestonesEvents.searchParam
                    )
                )
            }

            is AllMilestonesEvents.SelectMilestoneStatus -> {
                if (getSuccessScreenState().data.selectedMilestoneStatus == milestonesEvents.milestoneStatus) {
                    return
                }
                _screenStates.value = getSuccessScreenState().copy(
                    data = getSuccessScreenState().data.copy(
                        selectedMilestoneStatus = milestonesEvents.milestoneStatus
                    )
                )
            }

            is AllMilestonesEvents.PassMilestone -> {
                getMilestoneById(milestonesEvents.milestoneId)
            }

            is AllMilestonesEvents.ToggleTaskState -> {
                _stateTasks.find {
                    it.taskId == milestonesEvents.taskId
                }?.let { foundTaskState ->
                    foundTaskState.statusState = !foundTaskState.statusState
                }

                val tasks = tasksUseCase.transformTasksUseCase.transformTaskStatesToTasks(
                    stateTasks
                )

                // Update db
                viewModelScope.launch {
                    checkAndUpdateMilestoneStatus(tasks)
                    tasksUseCase.insertOrUpdateTasksUseCase(tasks)
                    checkAndUpdateProjectStatus()
                }
            }
        }
    }

    private fun getSuccessScreenState() =
        (screenStates.value as ScreenState.Data<AllMilestonesStates>)

    private fun getNullableSuccessScreenState() =
        (screenStates.value as? ScreenState.Data<AllMilestonesStates>)

    private fun getMilestoneById(milestoneId: Int) {
        viewModelScope.launch {
            getAllMilestonesJob?.cancel()
            getAllMilestonesJob = when (
                val milestone =
                    milestonesUseCases.getMilestoneByIdWithTasksUseCase(milestoneId)
            ) {
                is DataResult.Error -> TODO()
                is DataResult.Success -> {
                    milestone.data.onEach { milestoneWithTask ->
                        _screenStates.value = getSuccessScreenState().copy(
                            data = getSuccessScreenState().data.copy(
                                mileStone = milestoneWithTask
                            )
                        )
                        // adding tasks to state
                        _stateTasks.apply {
                            clear()
                            addAll(
                                tasksUseCase.transformTasksUseCase.transformTasksToTaskStates(
                                    milestoneWithTask!!.tasks
                                )
                            )
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    private suspend fun checkAndUpdateMilestoneStatus(tasks: List<Task>) {
        val currentMilestoneStatus =
            milestonesUseCases.checkMilestoneStatusUseCase.invoke(tasks)
        getSuccessScreenState().data.mileStone?.let {
            milestonesUseCases.insertOrUpdateMilestoneUseCase(
                it.milestone.copy(status = currentMilestoneStatus)
            )
        }
    }

    private fun checkAndUpdateProjectStatus() {
        viewModelScope.launch {

            val projectId =
                getSuccessScreenState().data.mileStone?.milestone?.projectId ?: return@launch
            val projectStatus = when (
                val status =
                    projectsUseCases.checkProjectStatusUseCase.invoke(projectId)
            ) {
                is DataResult.Error -> status.errorMessage
                is DataResult.Success -> status.data
            }
            when (val result = projectsUseCases.getProjectByIdUseCase(projectId)) {
                is DataResult.Error -> TODO()
                is DataResult.Success -> {
                    result.data.collectLatest { project ->
                        projectsUseCases.updateProjectsUseCase.invoke(
                            project.copy(projectStatus = projectStatus)
                        )
                    }
                }
            }
        }
    }
}
