package com.mumbicodes.projectie.presentation.projectDetails

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.use_case.tasks.TasksUseCases
import com.mumbicodes.projectie.presentation.add_edit_milestone.TaskState
import com.mumbicodes.projectie.presentation.util.PROJECT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val projectsUseCases: ProjectsUseCases,
    private val milestonesUseCases: MilestonesUseCases,
    private val tasksUseCase: TasksUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = mutableStateOf(ProjectDetailsStates())
    val state = _state

    private var getMilestonesJob: Job? = null
    private var getProjectJob: Job? = null

    private val projectId = savedStateHandle.get<Int>(PROJECT_ID)

    private val _uiEvents = MutableSharedFlow<ProjectUIEvents>()
    val uiEvents = _uiEvents

    private var _stateTasks = mutableListOf<TaskState>().toMutableStateList()
    val stateTasks: List<TaskState> = _stateTasks

    init {
        projectId?.let { projectId ->
            getProjectDetails(projectId = projectId)
        }
    }

    fun getProjectDetails(projectId: Int) {
        viewModelScope.launch {
            // todo delete
            projectsUseCases.getProjectByIdUseCase(projectId).also { projectPassed ->
                _state.value = _state.value.copy(
                    project = projectPassed,
                )
            }

            getProject(projectId, state.value.selectedMilestoneStatus)
        }
    }

    fun onEvent(projectDetailsEvents: ProjectDetailsEvents) {
        when (projectDetailsEvents) {
            is ProjectDetailsEvents.SelectedMilestonesStatus -> {
                if (state.value.selectedMilestoneStatus == projectDetailsEvents.milestoneStatus) {
                    return
                }
                state.value.milestones.filterMilestones(projectDetailsEvents.milestoneStatus)
                // getProject(projectId!!, projectDetailsEvents.milestoneStatus)
            }
            is ProjectDetailsEvents.GetMilestone -> {
                getMilestoneById(projectDetailsEvents.milestoneId)
            }
            is ProjectDetailsEvents.DeleteProject -> {
                viewModelScope.launch {
                    projectsUseCases.deleteProjectUseCase(projectDetailsEvents.project)

                    uiEvents.emit(ProjectUIEvents.DeleteProject)
                }
            }
            is ProjectDetailsEvents.DeleteMilestone -> {
                viewModelScope.launch {
                    milestonesUseCases.deleteMilestoneUseCase(projectDetailsEvents.milestone)

                    uiEvents.emit(ProjectUIEvents.DeleteMilestone)
                }
            }
            is ProjectDetailsEvents.ToggleMenuOptionsVisibility -> {
                _state.value = _state.value.copy(
                    isMenuOptionsVisible = !state.value.isMenuOptionsVisible
                )
            }
            is ProjectDetailsEvents.ToggleDeleteDialogVisibility -> {
                _state.value = _state.value.copy(
                    isDeleteDialogVisible = !state.value.isDeleteDialogVisible
                )
            }
            is ProjectDetailsEvents.ToggleCongratulationsDialogVisibility -> {
                _state.value = _state.value.copy(
                    isCongratsDialogVisible = !state.value.isCongratsDialogVisible
                )
            }
            is ProjectDetailsEvents.ToggleTaskState -> {
                _stateTasks.find {
                    it.taskId == projectDetailsEvents.taskId
                }?.let { foundTaskState ->
                    foundTaskState.statusState = !foundTaskState.statusState
                }
                val tasks = tasksUseCase.transformTasksUseCase.transformTaskStatesToTasks(
                    stateTasks
                )
                checkAndUpdateMilestoneStatus(tasks)
                // Update db
                viewModelScope.launch {
                    tasksUseCase.addTasksUseCase(
                        tasks
                    )
                }
            }
        }
    }

    // TODO delete this
    /*  private fun getProjectMilestones(projectId: Int, milestoneStatus: String) {
          getMilestonesJob?.cancel()
          getMilestonesJob =
              milestonesUseCases.getMilestonesUseCase(projectId, milestoneStatus)
                  .onEach { milestones ->
                      _state.value = _state.value.copy(
                          milestones = milestones,
                          selectedMilestoneStatus = milestoneStatus
                      )
                  }
                  .launchIn(viewModelScope)
      }*/

    private fun getMilestoneById(milestoneId: Int) {
        getMilestonesJob?.cancel()
        getMilestonesJob = milestonesUseCases.getMilestoneByIdWithTasksUseCase(milestoneId)
            .onEach { milestoneWithTask ->
                _state.value = _state.value.copy(
                    mileStone = milestoneWithTask ?: MilestoneWithTasks(
                        milestone = Milestone(
                            projectId = 0,
                            milestoneId = 0,
                            milestoneTitle = "",
                            milestoneSrtDate = 0,
                            milestoneEndDate = 0,
                            status = "",
                        ),
                        tasks = listOf()
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
            }
            .launchIn(viewModelScope)
    }

    /**
     * Because the query returns a flow which is reactive, had to make the return type nullable
     * Needed when someone deletes a project before navigating up
     * */
    private fun getProject(projectId: Int, milestoneStatus: String) {
        getProjectJob?.cancel()
        getProjectJob = projectsUseCases.getProjectByIdWithMilestonesUseCase(projectId)
            .onEach { projectWithMilestones ->
                _state.value = state.value.copy(
                    project = projectWithMilestones?.project ?: Project(
                        projectId = 0,
                        projectName = "",
                        projectDesc = "",
                        projectDeadline = "",
                        projectStatus = "",
                        timeStamp = 1
                    ),
                    milestones = projectWithMilestones?.milestones ?: emptyList(),
                    filteredMilestones = projectWithMilestones?.milestones ?: emptyList(),
                    selectedMilestoneStatus = milestoneStatus,
                )
            }
            .launchIn(viewModelScope)
    }

    private fun List<MilestoneWithTasks>.filterMilestones(
        milestoneStatus: String,
    ) {
        _state.value = state.value.copy(
            filteredMilestones = if (milestoneStatus == "All") {
                this
            } else {
                this.filter {
                    it.milestone.status == milestoneStatus
                }
            },
            selectedMilestoneStatus = milestoneStatus,
        )
    }

    private fun checkAndUpdateMilestoneStatus(tasks: List<Task>) {
        viewModelScope.launch {
            val currentMilestoneStatus =
                milestonesUseCases.checkMilestoneStatusUseCase.invoke(tasks)
            milestonesUseCases.addMilestoneUseCase(
                state.value.mileStone.milestone.copy(
                    status = currentMilestoneStatus
                )
            )
        }
    }
}
