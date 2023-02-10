package com.mumbicodes.projectie.presentation.add_edit_milestone

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.projectie.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.projectie.domain.use_case.tasks.TasksUseCases
import com.mumbicodes.projectie.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMilestonesViewModel @Inject constructor(
    private val milestonesUseCases: MilestonesUseCases,
    private val tasksUseCases: TasksUseCases,
    private val projectsUseCases: ProjectsUseCases,
    savedStateHandle: SavedStateHandle,
    private val appContext: Application,
) : ViewModel() {

    private val _milestoneTitleState = mutableStateOf("")
    val milestoneTitleState = _milestoneTitleState

    private val _milestoneStartDateState: MutableState<String> = mutableStateOf("")
    val milestoneStartDateState: State<String> = _milestoneStartDateState

    private val _milestoneEndDateState: MutableState<String> = mutableStateOf("")
    val milestoneEndDateState: State<String> = _milestoneEndDateState

    private val _isCalendarVisible = mutableStateOf(false)
    val isCalendarVisible = _isCalendarVisible

    // tasks from the db and to
    private var _storeTasks = listOf<Task>()
    val storedTasks = _storeTasks

    // tasks from the screen and to screen
    private var _stateTasks = mutableListOf<TaskState>().toMutableStateList()
    val stateTasks: List<TaskState> = _stateTasks

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents

    // Default milestone status is "Not Started" since it doesn't have any task
    private var currentMilestoneStatus: String = "Not Started"

    private var currentMilestoneId: Int? = null
    private var passedProjectId: Int = savedStateHandle.get<Int>(PROJECT_ID)!!
    var passedMilestoneId: Int? = savedStateHandle.get<Int>(MILESTONE_ID)

    private var getMilestonesJob: Job? = null

    init {
        passedMilestoneId?.let { milestoneId ->
            if (milestoneId != -1) {

                getMilestoneByIdWithTasks(milestoneId)

                // TODO remove hint visibility for tasks
            } else {
                currentMilestoneId = System.currentTimeMillis().toInt()
                // If new milestone, add one task
                // TODO make this a reusable function
                addNewTaskState()
            }
        }
    }

    fun onEvent(addEditMilestoneEvents: AddEditMilestoneEvents) {
        when (addEditMilestoneEvents) {
            is AddEditMilestoneEvents.MilestoneTitleChanged -> {
                _milestoneTitleState.value = addEditMilestoneEvents.value
            }
            is AddEditMilestoneEvents.ToggleCalendarVisibility -> {
                _isCalendarVisible.value = !isCalendarVisible.value
            }
            is AddEditMilestoneEvents.MilestoneStartDateChanged -> {
                _milestoneStartDateState.value =
                    addEditMilestoneEvents.value.toDateAsString("dd/MM/yyyy")
                _isCalendarVisible.value = !isCalendarVisible.value
            }
            is AddEditMilestoneEvents.MilestoneEndDateChanged -> {
                _milestoneEndDateState.value =
                    addEditMilestoneEvents.value.toDateAsString("dd/MM/yyyy")
                _isCalendarVisible.value = !isCalendarVisible.value
            }
            // on clicking + for tasks
            is AddEditMilestoneEvents.NewTaskAdded -> {
                addNewTaskState()
            }
            is AddEditMilestoneEvents.DeleteTask -> {
                deleteTaskFromState(addEditMilestoneEvents.task)
            }
            is AddEditMilestoneEvents.ChangeTaskDescFocus -> {
                _stateTasks.find {
                    it.taskId == addEditMilestoneEvents.task.taskId
                }?.let { foundTaskState ->
                    foundTaskState.taskDescState = foundTaskState.taskDescState.copy(
                        isHintVisible = !addEditMilestoneEvents.focusState.isFocused &&
                            foundTaskState.taskDescState.text.isBlank()
                    )
                }
            }
            is AddEditMilestoneEvents.ChangeTaskTitleFocus -> {
                _stateTasks.find {
                    it.taskId == addEditMilestoneEvents.task.taskId
                }?.let { foundTaskState ->
                    foundTaskState.taskTitleState = foundTaskState.taskTitleState.copy(
                        isHintVisible = !addEditMilestoneEvents.focusState.isFocused &&
                            foundTaskState.taskTitleState.text.isBlank()
                    )
                }
            }
            is AddEditMilestoneEvents.TaskDescChanged -> {
                _stateTasks.find {
                    it.taskId == addEditMilestoneEvents.task.taskId
                }?.let { foundTaskState ->
                    foundTaskState.taskDescState = foundTaskState.taskDescState.copy(
                        text = addEditMilestoneEvents.value
                    )
                }
            }
            is AddEditMilestoneEvents.TaskTitleChanged -> {
                _stateTasks.find {
                    it.taskId == addEditMilestoneEvents.task.taskId
                }?.let { foundTaskState ->
                    foundTaskState.taskTitleState = foundTaskState.taskTitleState.copy(
                        text = addEditMilestoneEvents.value
                    )
                    Log.d(
                        "Task changed",
                        "${foundTaskState.taskId} = ${foundTaskState.taskTitleState}  + ${foundTaskState.taskDescState} "
                    )
                }
            }
            is AddEditMilestoneEvents.ToggleTaskStatus -> {
                _stateTasks.find {
                    it.taskId == addEditMilestoneEvents.task.taskId
                }?.let { foundTaskState ->
                    foundTaskState.statusState = !foundTaskState.statusState
                }
            }
            is AddEditMilestoneEvents.AddEditMilestone -> {
                val tasks = tasksUseCases.transformTasksUseCase.transformTaskStatesToTasks(
                    stateTasks
                )
                checkMilestoneStatus(tasks)

                viewModelScope.launch {

                    milestonesUseCases.addMilestoneUseCase(
                        Milestone(
                            projectId = passedProjectId,
                            milestoneId = currentMilestoneId!!,
                            milestoneTitle = milestoneTitleState.value,
                            milestoneSrtDate = milestoneStartDateState.value.toLocalDate("dd/MM/yyyy")
                                .toLong(),
                            milestoneEndDate = milestoneEndDateState.value.toLocalDate("dd/MM/yyyy")
                                .toLong(),
                            status = currentMilestoneStatus,
                        )
                    )

                    tasksUseCases.addTasksUseCase(
                        tasksUseCases.transformTasksUseCase.transformTaskStatesToTasks(
                            stateTasks
                        ).filter {
                            it.taskTitle.isNotBlank() && it.taskTitle.isNotEmpty()
                        }
                    )
                    uiEvents.emit(UIEvents.AddEditMilestone)

                    checkAndUpdateProjectStatus()
                }
            }
        }
    }

    private fun getMilestoneByIdWithTasks(milestoneId: Int) {
        getMilestonesJob?.cancel()
        getMilestonesJob = milestonesUseCases.getMilestoneByIdWithTasksUseCase(milestoneId)
            .onEach { milestoneWithTask ->
                currentMilestoneId = milestoneId
                _milestoneTitleState.value = milestoneWithTask!!.milestone.milestoneTitle
                _milestoneStartDateState.value =
                    milestoneWithTask.milestone.milestoneSrtDate.toDateAsString("dd/MM/yyyy")
                _milestoneEndDateState.value =
                    milestoneWithTask.milestone.milestoneEndDate.toDateAsString("dd/MM/yyyy")
                currentMilestoneStatus = milestoneWithTask.milestone.status
                _stateTasks.apply {
                    addAll(
                        tasksUseCases.transformTasksUseCase.transformTasksToTaskStates(
                            milestoneWithTask.tasks
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun addNewTaskState() {
        // val randomNumber = Random.nextInt()
        val randomNumber = System.currentTimeMillis().toInt()
        _stateTasks.add(
            TaskState(
                taskId = randomNumber,
                milestoneId = currentMilestoneId!!
            )
        )
    }

    private fun deleteTaskFromState(taskState: TaskState) {
        viewModelScope.launch {
            tasksUseCases.deleteTaskUseCase(
                taskState.toTask()
            )
        }
        _stateTasks.remove(taskState)
    }

    fun checkMilestoneStatus(tasks: List<Task>) {
        viewModelScope.launch {
            currentMilestoneStatus = milestonesUseCases.checkMilestoneStatusUseCase.invoke(tasks)
        }
    }

    fun checkAndUpdateProjectStatus() {
        viewModelScope.launch {

            val projectStatus = projectsUseCases.checkProjectStatusUseCase.invoke(passedProjectId)

            val project: Project = projectsUseCases.getProjectByIdUseCase(passedProjectId)

            projectsUseCases.updateProjectsUseCase.invoke(
                project.copy(projectStatus = projectStatus)
            )
        }
    }
}
