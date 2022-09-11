package com.mumbicodes.presentation.add_edit_milestone

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AddEditMilestonesViewModel(
    private val milestonesUseCases: MilestonesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _milestoneTitleState = mutableStateOf("")
    val milestoneTitleState = _milestoneTitleState

    private val _milestoneStartDateState: MutableState<String> = mutableStateOf("")
    val milestoneStartDateState: State<String> = _milestoneStartDateState

    private val _milestoneEndDateState: MutableState<String> = mutableStateOf("")
    val milestoneEndDateState: State<String> = _milestoneEndDateState

    private val _taskTitleState = mutableStateOf("")
    val taskTitleState = _taskTitleState

    private val _taskDescState = mutableStateOf("")
    val taskDescState = _taskDescState

    private val _taskStates = mutableStateOf(TaskStates())
    val taskStates = _taskStates

    private val _isCalendarVisible = mutableStateOf(false)
    val isCalendarVisible = _isCalendarVisible

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents

    // Default milestone status is "Not Started" since it doesn't have any task
    private var currentMilestoneStatus: String = "Not Started"

    private var tasksToAdd = mutableListOf<Task>()
    private var currentMilestoneId: Int = 0
    private var passedProjectId: Int = savedStateHandle.get<Int>(PROJECT_ID)!!
    var passedMilestoneId: Int? = savedStateHandle.get<Int>(MILESTONE_ID)

    init {
        passedMilestoneId?.let { milestoneId ->
            if (milestoneId != -1) {
                viewModelScope.launch {

                    milestonesUseCases.getMilestoneByIdUseCase(milestoneId).also { milestone ->
                        currentMilestoneId = milestoneId
                        _milestoneTitleState.value = milestone.milestoneTitle
                        _milestoneStartDateState.value =
                            milestone.milestoneEndDate.toDateAsString("dd/MM/yyyy")
                        _milestoneEndDateState.value =
                            milestone.milestoneSrtDate.toDateAsString("dd/MM/yyyy")
                        currentMilestoneStatus = milestone.status
                        _taskStates.value = _taskStates.value.copy(
                            tasks = milestone.tasks
                        )
                    }
                }
            }
        }
    }

    fun onEvent(addEditMilestoneEvents: AddEditMilestoneEvents) {
        when (addEditMilestoneEvents) {
            is AddEditMilestoneEvents.TitleChanged -> {
                _milestoneTitleState.value = addEditMilestoneEvents.value
            }
            is AddEditMilestoneEvents.StartDateChanged -> {
                _milestoneStartDateState.value =
                    addEditMilestoneEvents.value.toDateAsString("dd/MM/yyyy")
            }
            is AddEditMilestoneEvents.EndDateChanged -> {
                _milestoneEndDateState.value =
                    addEditMilestoneEvents.value.toDateAsString("dd/MM/yyyy")
            }
            is AddEditMilestoneEvents.TaskUpdated -> {
                tasksToAdd.add(addEditMilestoneEvents.value)
                _taskStates.value = _taskStates.value.copy(
                    tasks = tasksToAdd
                )
            }
            is AddEditMilestoneEvents.ToggleCalendarVisibility -> {
                _isCalendarVisible.value = !isCalendarVisible.value
            }
            // TODO check for status the best way and also update project status based on it milestones
            is AddEditMilestoneEvents.AddEditProject -> {
                viewModelScope.launch {
                    milestonesUseCases.addMilestoneUseCase(
                        Milestone(
                            projectId = passedProjectId,
                            milestoneId = currentMilestoneId,
                            milestoneTitle = milestoneTitleState.value,
                            milestoneSrtDate = milestoneStartDateState.value.toLocalDate("dd/MM/yyyy")
                                .toLong(),
                            milestoneEndDate = milestoneEndDateState.value.toLocalDate("dd/MM/yyyy")
                                .toLong(),
                            status = currentMilestoneStatus,
                            tasks = taskStates.value.tasks
                        )
                    )
                    uiEvents.emit(UIEvents.AddEditMilestone)
                }
            }
        }
    }
}
