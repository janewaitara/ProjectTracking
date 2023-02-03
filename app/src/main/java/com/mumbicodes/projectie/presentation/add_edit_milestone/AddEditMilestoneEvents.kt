package com.mumbicodes.projectie.presentation.add_edit_milestone

import androidx.compose.ui.focus.FocusState
import java.time.LocalDate

sealed class AddEditMilestoneEvents {
    data class MilestoneTitleChanged(val value: String) : AddEditMilestoneEvents()
    object ToggleCalendarVisibility : AddEditMilestoneEvents()
    data class MilestoneStartDateChanged(val value: LocalDate) : AddEditMilestoneEvents()
    data class MilestoneEndDateChanged(val value: LocalDate) : AddEditMilestoneEvents()
    object NewTaskAdded : AddEditMilestoneEvents()
    data class DeleteTask(val task: TaskState) : AddEditMilestoneEvents()
    data class TaskTitleChanged(val task: TaskState, val value: String) : AddEditMilestoneEvents()
    data class ChangeTaskTitleFocus(val task: TaskState, val focusState: FocusState) : AddEditMilestoneEvents()
    data class TaskDescChanged(val task: TaskState, val value: String) : AddEditMilestoneEvents()
    data class ChangeTaskDescFocus(val task: TaskState, val focusState: FocusState) : AddEditMilestoneEvents()
    data class ToggleTaskStatus(val task: TaskState) : AddEditMilestoneEvents()
    object AddEditMilestone : AddEditMilestoneEvents()
}

sealed class UIEvents {
    object AddEditMilestone : UIEvents()
    data class ShowSnackBar(val message: String) : UIEvents()
}
