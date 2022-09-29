package com.mumbicodes.presentation.add_edit_milestone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mumbicodes.R
import com.mumbicodes.presentation.components.LabelledInputField
import com.mumbicodes.presentation.components.LabelledInputFieldWithIcon
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.components.TaskItemField
import com.mumbicodes.presentation.theme.*
import com.squaredem.composecalendar.ComposeCalendar
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
    val tasksState = milestonesViewModel.stateTasks

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
            .fillMaxWidth()
            .padding(Space20dp)
            .background(color = MaterialTheme.colorScheme.background)
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
            modifier = Modifier.fillMaxWidth(),
            onTitleChanged = { title ->
            },

            onDatesClicked = {
                milestonesViewModel.onEvent(AddEditMilestoneEvents.ToggleCalendarVisibility)
            },
            onStartDateChanged = { startDate ->
            },
            onEndDateChanged = { endDate ->
            },
            titleTextValue = milestoneTitleState,
            startDateTextValue = milestoneStartDateState,
            endDateTextValue = milestoneEndDateState,
            isCalendarVisible = isCalendarVisible,
            onSaveMilestone = {
            },
            tasks = tasksState,
            addNewTaskInViewModel = {
                // todo add a new task in the viewModel
            },
            onCheckedChange = { task ->
                // TODO update the task status
            },
            onTaskTitleChange = { task, taskTitle ->
                // TODO update the task title
            },
            onTaskDescChange = { task, taskDesc ->
                // TODO update the task desc
            },
            onTaskTitleFocusChange = {
                // TODO update the task title focus
            },
            onTaskDescFocusChange = {
                // TODO update the task desc focus
            },
        )
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
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "Back button",
        )
        Text(
            text = stringResource(id = R.string.addEditMilestoneTitle, titleTextAction),
            style = MaterialTheme.typography.headlineLarge.copy(color = GreyDark),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )
    }
}

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
    onSaveMilestone: () -> Unit,
    tasks: List<TaskState>,
    addNewTaskInViewModel: () -> Unit,
    onCheckedChange: (TaskState) -> Unit,
    onTaskTitleChange: (TaskState, String) -> Unit,
    onTaskDescChange: (TaskState, String) -> Unit,
    onTaskTitleFocusChange: (FocusState) -> Unit,
    onTaskDescFocusChange: (FocusState) -> Unit,
) {

    // helps determine which date to update
    var calendarTrigger = ""

    Column(modifier = modifier) {
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
            ComposeCalendar(
                onDone = { userDateSelection ->
                    if (calendarTrigger == "StartDate") {
                        onStartDateChanged(userDateSelection)
                    } else {
                        onEndDateChanged(userDateSelection)
                    }
                },
                onDismiss =
                onDatesClicked
            )
        }

        Spacer(modifier = Modifier.height(Space20dp))

        Row(modifier = Modifier.fillMaxWidth()) {
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
        tasks.forEach { task ->
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
                onTaskTitleFocusChange = {
                    onTaskTitleFocusChange(it)
                },
                onTaskDescFocusChange = {
                    onTaskDescFocusChange(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(Space48dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.saveProject),
            onClick = onSaveMilestone,
            isEnabled = titleTextValue.isNotBlank() &&
                startDateTextValue.isNotBlank() &&
                endDateTextValue.isNotBlank()
        )
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
            onSaveMilestone = {
            },
            tasks = listOf(
                TaskState(
                    taskTitleState = TaskTextFieldState(
                        text = "Task Title 1"
                    ),
                    taskDescState = TaskTextFieldState(
                        text = "Task Desc 1"
                    )
                ),
                TaskState(
                    taskTitleState = TaskTextFieldState(
                        text = "Task Title 2"
                    ),
                    taskDescState = TaskTextFieldState(
                        text = "Task Desc 2"
                    )
                ),
            ),
            addNewTaskInViewModel = {},
            onCheckedChange = {},
            onTaskTitleChange = { _, _ -> },
            onTaskDescChange = { _, _ -> },
            onTaskTitleFocusChange = {},
            onTaskDescFocusChange = {}
        )
    }
}
