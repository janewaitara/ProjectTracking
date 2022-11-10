package com.mumbicodes.presentation.add_edit_milestone

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.R
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.domain.use_case.projects.ProjectsUseCases
import com.mumbicodes.domain.use_case.tasks.TasksUseCases
import com.mumbicodes.domain.util.ProgressStatus
import com.mumbicodes.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
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

    private val _tasks = mutableStateListOf<Task>()

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
    private var getProjectMilestonesJob: Job? = null

    private var _projectProgress: ProgressStatus = ProgressStatus.InProgress(
        appContext.getString(R.string.inProgress)
    )

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
            // TODO check for status the best way and also update project status based on it milestones
            is AddEditMilestoneEvents.AddEditMilestone -> {
                val tasks = transformTaskStatesToTasks(stateTasks)
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
                    Log.d("Tasks", "${stateTasks.size}")

                    tasksUseCases.addTasksUseCase(
                        transformTaskStatesToTasks(stateTasks)
                    )
                    Log.d(
                        "Task add",
                        stateTasks.map {
                            it.toTask()
                        }.first().taskTitle
                    )
                    uiEvents.emit(UIEvents.AddEditMilestone)

                    Log.d("Reached 1", "Reached")
                }

                checkAndUpdateProjectStatus()
                Log.d("Reached 2", "Reached")
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
                    addAll(transformTasksToTaskStates(milestoneWithTask.tasks))
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

    fun Task.toTaskState() = TaskState(
        milestoneId = milestoneId,
        taskId = taskId,
        initialTaskTitleState = TaskTextFieldState(
            text = taskTitle
        ),
        initialTaskDescState = TaskTextFieldState(
            text = taskDesc
        ),
        initialStatusState = status,
    )

    fun TaskState.toTask() = Task(
        milestoneId = milestoneId,
        taskId = taskId,
        taskTitle = taskTitleState.text,
        taskDesc = taskDescState.text,
        status = statusState
    )

    fun transformTasksToTaskStates(tasks: List<Task>): List<TaskState> {
        return tasks.map { task ->
            task.toTaskState()
        }
    }

    fun transformTaskStatesToTasks(taskStates: List<TaskState>): List<Task> =
        taskStates.map { taskState ->
            taskState.toTask()
        }

    fun checkMilestoneStatus(tasks: List<Task>) {
        viewModelScope.launch {
            val progress = milestonesUseCases.checkMilestoneStatusUseCase.invoke(tasks)

            currentMilestoneStatus = when (progress) {

                is ProgressStatus.Completed -> progress.status
                is ProgressStatus.InProgress -> progress.status
                is ProgressStatus.NotStarted -> progress.status
            }
        }
    }

    fun checkAndUpdateProjectStatus() {
        viewModelScope.launch {

            Log.e("Mini me", "This passes")
            // val progress = projectsUseCases.checkProjectStatusUseCase.invoke(passedProjectId)
            checkStatus(passedProjectId)
            Log.e("Mini me", "This passes too")
            val pro = when (_projectProgress) {

                is ProgressStatus.Completed -> _projectProgress.status
                is ProgressStatus.InProgress -> _projectProgress.status
                is ProgressStatus.NotStarted -> _projectProgress.status
            }
            Log.e("Mini me 1", pro)

            // TODO complete the logic to update the project status update - Test the function below
            val project: Project = projectsUseCases.getProjectByIdUseCase(passedProjectId)

            /*  projectsUseCases.updateProjectsUseCase.invoke(
                  project.copy(projectStatus = when (progress) {

                      is ProgressStatus.Completed -> progress.status
                      is ProgressStatus.InProgress -> progress.status
                      is ProgressStatus.NotStarted -> progress.status
                  })
              )*/
        }
    }

    fun checkStatus(projectId: Int) {

        Log.e("Mini me ", "Wierd, right")
        getProjectMilestonesJob?.cancel()
        getProjectMilestonesJob =
            milestonesUseCases.getMilestonesUseCase(projectId = projectId, status = "")
                .onEach { milestones ->
                    val milestonesStatus: List<String> = milestones.map {
                        it.status
                    }
                    val completed =
                        milestonesStatus.contains(appContext.getString(R.string.completed)) &&
                            !milestonesStatus.contains(appContext.getString(R.string.notStarted)) &&
                            !milestonesStatus.contains(appContext.getString(R.string.inProgress))
                    Log.e("Mini me zl", "${milestonesStatus.size}")

                    val notStarted =
                        milestonesStatus.contains(appContext.getString(R.string.notStarted)) &&
                            !milestonesStatus.contains(appContext.getString(R.string.completed)) &&
                            !milestonesStatus.contains(appContext.getString(R.string.inProgress))

                    _projectProgress = when {
                        completed -> ProgressStatus.Completed(appContext.getString(R.string.completed))
                        notStarted -> ProgressStatus.NotStarted(appContext.getString(R.string.notStarted))
                        else -> ProgressStatus.InProgress(appContext.getString(R.string.inProgress))
                    }
                    Log.e("Mini me zl 2", "$completed")
                }.launchIn(viewModelScope)
    }
}
