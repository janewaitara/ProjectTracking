package com.mumbicodes.projectie.presentation.add_edit_milestone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.components.*
import com.mumbicodes.projectie.presentation.theme.*
import com.mumbicodes.projectie.presentation.util.fromMillisToLocalDate
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun AddAndEditMilestoneScreen(
    milestonesViewModel: AddEditMilestonesViewModel = hiltViewModel(),
    onAddEditMilestone: () -> Unit,
    onClickIconBack: () -> Unit,
) {

    val milestoneTitleState = milestonesViewModel.milestoneTitleState.value
    val milestoneStartDateState = milestonesViewModel.milestoneStartDateState.value
    val milestoneEndDateState = milestonesViewModel.milestoneEndDateState.value
    val passedMilestoneId = milestonesViewModel.passedMilestoneId
    val isCalendarVisible = milestonesViewModel.isCalendarVisible.value
    val tasksState = milestonesViewModel.stateTasks.distinctBy { it.taskId }

    LaunchedEffect(key1 = true) {
        milestonesViewModel.uiEvents.collectLatest { uIEvents ->
            when (uIEvents) {
                is UIEvents.ShowSnackBar -> {
                    // TODO
                }
                is UIEvents.AddEditMilestone -> {
                    onAddEditMilestone()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(Space20dp)
    ) {
        val actionToPerform = when {
            passedMilestoneId != -1 -> stringResource(id = R.string.editTitle)
            else -> stringResource(id = R.string.addTitle)
        }

        ScreenHeader(
            modifier = Modifier,
            titleTextAction = actionToPerform,
            iconOnClick = {
                onClickIconBack()
            }
        )

        FieldForms(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onTitleChanged = { title ->
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.MilestoneTitleChanged(title)
                )
            },
            onDatesClicked = {
                milestonesViewModel.onEvent(AddEditMilestoneEvents.ToggleCalendarVisibility)
            },
            onStartDateChanged = { startDate ->
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.MilestoneStartDateChanged(startDate)
                )
            },
            onEndDateChanged = { endDate ->
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.MilestoneEndDateChanged(endDate)
                )
            },
            titleTextValue = milestoneTitleState,
            startDateTextValue = milestoneStartDateState,
            endDateTextValue = milestoneEndDateState,
            isCalendarVisible = isCalendarVisible,
            tasks = tasksState,
            addNewTaskInViewModel = {
                // todo add a new task in the viewModel
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.NewTaskAdded
                )
            },
            onCheckedChange = { task ->
                // TODO update the task status
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.ToggleTaskStatus(task)
                )
            },
            onTaskTitleChange = { task, taskTitle ->
                // TODO update the task title
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.TaskTitleChanged(task, taskTitle)
                )
            },
            onTaskDescChange = { task, taskDesc ->
                // TODO update the task desc
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.TaskDescChanged(task, taskDesc)
                )
            },
            onTaskTitleFocusChange = { task, focusState ->
                // TODO update the task title focus
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.ChangeTaskTitleFocus(task, focusState)
                )
            },
            onTaskDescFocusChange = { task, focusState ->
                // TODO update the task desc focus
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.ChangeTaskDescFocus(task, focusState)
                )
            },
            onTaskSwiped = { task ->
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.DeleteTask(task)
                )
            }
        )
        Spacer(modifier = Modifier.height(Space48dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.saveMilestone),
            onClick = {
                milestonesViewModel.onEvent(
                    AddEditMilestoneEvents.AddEditMilestone
                )
            },
            isEnabled = milestoneTitleState.isNotBlank() &&
                milestoneStartDateState.isNotBlank() &&
                milestoneEndDateState.isNotBlank()
        )

        Spacer(modifier = Modifier.height(Space24dp))
    }
}

@Composable
fun ScreenHeader(
    modifier: Modifier,
    titleTextAction: String,
    iconOnClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp, 24.dp)
                .clickable {
                    iconOnClick()
                },
            painter = painterResource(id = R.drawable.ic_arrow_back),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "Back button",
        )
        Text(
            text = stringResource(id = R.string.addEditMilestoneTitle, titleTextAction),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FieldForms(
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    onDatesClicked: () -> Unit,
    onStartDateChanged: (LocalDate) -> Unit,
    onEndDateChanged: (LocalDate) -> Unit,
    titleTextValue: String,
    startDateTextValue: String,
    endDateTextValue: String,
    isCalendarVisible: Boolean,
    tasks: List<TaskState>,
    addNewTaskInViewModel: () -> Unit,
    onCheckedChange: (TaskState) -> Unit,
    onTaskTitleChange: (TaskState, String) -> Unit,
    onTaskDescChange: (TaskState, String) -> Unit,
    onTaskTitleFocusChange: (TaskState, FocusState) -> Unit,
    onTaskDescFocusChange: (TaskState, FocusState) -> Unit,
    onTaskSwiped: (TaskState) -> Unit,
) {

    // helps determine which date to update
    var calendarTrigger by remember {
        mutableStateOf("test")
    }
    val datePickerState = rememberDatePickerState()

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Spacer(modifier = Modifier.height(Space20dp))

            LabelledInputField(
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = onTitleChanged,
                fieldLabel = stringResource(id = R.string.milestoneTitle),
                placeholder = "Milestone Title",
                textValue = titleTextValue,
                singleLine = false,
            )
            Spacer(modifier = Modifier.height(Space20dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onDatesClicked()
                            calendarTrigger = "StartDate"
                        },
                    color = Color.Transparent
                ) {
                    LabelledInputFieldWithIcon(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = {},
                        fieldLabel = stringResource(id = R.string.milestoneStartDate),
                        placeholder = "DD/MM/YYYY",
                        textValue = startDateTextValue,
                        singleLine = true,
                        vectorIconId = R.drawable.ic_calendar,
                    )
                }

                Spacer(modifier = Modifier.width(Space8dp))

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onDatesClicked()
                            calendarTrigger = "EndDate"
                        },
                    color = Color.Transparent
                ) {
                    LabelledInputFieldWithIcon(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = {},
                        fieldLabel = stringResource(id = R.string.milestoneEndDate),
                        placeholder = "DD/MM/YYYY",
                        textValue = endDateTextValue,
                        singleLine = true,
                        vectorIconId = R.drawable.ic_calendar,
                    )
                }
            }

            if (isCalendarVisible) {
                /*ComposeCalendar(
                    onDone = { userDateSelection ->
                        if (calendarTrigger == "StartDate") {
                            onStartDateChanged(userDateSelection)
                        } else {
                            onEndDateChanged(userDateSelection)
                        }
                    },
                    onDismiss =
                    onDatesClicked
                )*/
                DatePickerDialog(
                    onDismissRequest = onDatesClicked,
                    confirmButton = {
                        TextButton(onClick = {
                            if (datePickerState.selectedDateMillis == null) {
                                onDatesClicked()
                            } else {
                                if (calendarTrigger == "StartDate") {
                                    onStartDateChanged(datePickerState.selectedDateMillis!!.fromMillisToLocalDate())
                                } else {
                                    onEndDateChanged(datePickerState.selectedDateMillis!!.fromMillisToLocalDate())
                                }
                            }
                        }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDatesClicked) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space20dp))
        }
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                // TODO - make the string one and update the caps area
                Text(
                    text = stringResource(id = R.string.miniTasksSmall),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(Space8dp))

                Icon(
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .clickable {
                            addNewTaskInViewModel()
                        },
                    painter = painterResource(id = R.drawable.ic_add_filled),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Add task",
                )
            }

            Spacer(modifier = Modifier.height(Space8dp))
        }

        items(tasks, { task: TaskState -> task.taskId }) { task ->
            SwipeToDismissComponent(
                onSwipeAction = {
                    onTaskSwiped(task)
                },
                content = {
                    TaskItemField(
                        modifier = Modifier,
                        task = task,
                        onCheckedChange = { onCheckedChange(task) },
                        onTaskTitleChange = { taskTitle ->
                            onTaskTitleChange(task, taskTitle)
                        },
                        onTaskDescChange = { taskDesc ->
                            onTaskDescChange(task, taskDesc)
                        },
                        onTaskTitleFocusChange = { focusState ->
                            onTaskTitleFocusChange(task, focusState)
                        },
                        onTaskDescFocusChange = { focusState ->
                            onTaskDescFocusChange(task, focusState)
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(Space8dp))
        }
    }
}

@Composable
@Preview
fun ScreenContentPreview() {
    ProjectTrackingTheme {

        FieldForms(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF)),
            onTitleChanged = { title ->
            },

            onDatesClicked = {
            },
            onStartDateChanged = { startDate ->
            },
            onEndDateChanged = { endDate ->
            },
            titleTextValue = "",
            startDateTextValue = "",
            endDateTextValue = "",
            isCalendarVisible = false,
            tasks = listOf(
                TaskState(
                    milestoneId = 234,
                    taskId = 432,
                    initialTaskTitleState = TaskTextFieldState(
                        text = "Task Title 1"
                    ),
                    initialTaskDescState = TaskTextFieldState(
                        text = "Task Desc 1"
                    )
                ),
                TaskState(
                    milestoneId = 234,
                    taskId = 433,
                    initialTaskTitleState = TaskTextFieldState(
                        text = "Task Title 2"
                    ),
                    initialTaskDescState = TaskTextFieldState(
                        text = "Task Desc 2"
                    )
                ),
            ),
            addNewTaskInViewModel = {},
            onCheckedChange = {},
            onTaskTitleChange = { _, _ -> },
            onTaskDescChange = { _, _ -> },
            onTaskTitleFocusChange = { _, _ -> },
            onTaskDescFocusChange = { _, _ -> },
            onTaskSwiped = { _ -> }
        )
    }
}
