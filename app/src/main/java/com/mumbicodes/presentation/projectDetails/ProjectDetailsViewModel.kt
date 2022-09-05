package com.mumbicodes.presentation.projectDetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.presentation.util.PROJECT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val projectsUseCases: ProjectsUseCases,
    private val milestonesUseCases: MilestonesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = mutableStateOf(ProjectDetailsStates())
    val state = _state

    private var getMilestonesJob: Job? = null

    private val projectId = savedStateHandle.get<Int>(PROJECT_ID)

    init {
        projectId?.let { projectId ->
            viewModelScope.launch {
                projectsUseCases.getProjectByIdUseCase(projectId).also { projectPassed ->
                    _state.value = _state.value.copy(
                        project = projectPassed,
                    )
                }
                getProjectMilestones(
                    projectId,
                    state.value.selectedMilestoneStatus
                )
            }
        }
    }

    fun onEvent(projectDetailsEvents: ProjectDetailsEvents) {
        when (projectDetailsEvents) {
            is ProjectDetailsEvents.SelectedMilestonesStatus -> {
                if (state.value.selectedMilestoneStatus == projectDetailsEvents.milestoneStatus) {
                    return
                }
                getProjectMilestones(projectId!!, projectDetailsEvents.milestoneStatus)
            }
            is ProjectDetailsEvents.GetMilestone -> {
                getMilestoneById(projectDetailsEvents.milestoneId)
            }
            is ProjectDetailsEvents.DeleteProject -> {
                viewModelScope.launch {
                    projectsUseCases.deleteProjectUseCase(projectDetailsEvents.project)
                }
            }
            is ProjectDetailsEvents.DeleteMilestone -> {
                viewModelScope.launch {
                    milestonesUseCases.deleteMilestoneUseCase(projectDetailsEvents.milestone)
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
        }
    }

    private fun getProjectMilestones(projectId: Int, milestoneStatus: String) {
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
    }

    private fun getMilestoneById(milestoneId: Int) {
        viewModelScope.launch {
            milestonesUseCases.getMilestoneByIdUseCase(milestoneId).also { milestone ->
                _state.value = _state.value.copy(
                    mileStone = milestone
                )
            }
        }
    }
}
