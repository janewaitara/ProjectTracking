package com.mumbicodes.presentation.projectDetails

import android.util.Log
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
    private var getProjectJob: Job? = null

    private val projectId = savedStateHandle.get<Int>(PROJECT_ID)

    init {
        projectId?.let { projectId ->
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
    }

    fun onEvent(projectDetailsEvents: ProjectDetailsEvents) {
        when (projectDetailsEvents) {
            is ProjectDetailsEvents.SelectedMilestonesStatus -> {
                if (state.value.selectedMilestoneStatus == projectDetailsEvents.milestoneStatus) {
                    return
                }
                getProject(projectId!!, projectDetailsEvents.milestoneStatus)
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
                    mileStone = milestoneWithTask
                )
            }
            .launchIn(viewModelScope)
    }

    // TODO: Think of How best can I filter the milestones to avoid fetching from DB all the time
    private fun getProject(projectId: Int, milestoneStatus: String) {
        getProjectJob?.cancel()
        getProjectJob = projectsUseCases.getProjectByIdWithMilestonesUseCase(projectId)
            .onEach { projectWithMilestones ->
                Log.e("Project", projectWithMilestones.toString())
                _state.value = state.value.copy(
                    project = projectWithMilestones.project,
                    milestones = if (milestoneStatus == "All") {
                        projectWithMilestones.milestones
                    } else {
                        projectWithMilestones.milestones.filter {
                            it.milestone.status == milestoneStatus
                        }
                    },
                    selectedMilestoneStatus = milestoneStatus,
                )
            }
            .launchIn(viewModelScope)
    }
}
