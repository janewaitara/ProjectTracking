package com.mumbicodes.presentation.add_edit_milestone

import com.mumbicodes.domain.model.Task
import java.time.LocalDate

sealed class AddEditMilestoneEvents {
    data class TitleChanged(val value: String) : AddEditMilestoneEvents()
    data class StartDateChanged(val value: LocalDate) : AddEditMilestoneEvents()
    data class EndDateChanged(val value: LocalDate) : AddEditMilestoneEvents()
    data class TaskUpdated(val value: Task) : AddEditMilestoneEvents()
    object ToggleCalendarVisibility : AddEditMilestoneEvents()
    object AddEditProject : AddEditMilestoneEvents()
}

sealed class UIEvents {
    object AddEditMilestone : UIEvents()
    data class ShowSnackBar(val message: String) : UIEvents()
}
