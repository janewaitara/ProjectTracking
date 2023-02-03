package com.mumbicodes.projectie.presentation.add_edit_project

import java.time.LocalDate

// These are one time actions
// TODO - SnackBar will be displayed after a successful addition
sealed class UIEvents {
    object AddEditProject : UIEvents()
    data class ShowSnackBar(val message: String) : UIEvents()
}

sealed class AddEditProjectEvents {
    data class NameChanged(val value: String) : AddEditProjectEvents()
    data class DescriptionChanged(val value: String) : AddEditProjectEvents()
    data class DeadlineChanged(val value: LocalDate) : AddEditProjectEvents()
    object ToggleCalendarVisibility : AddEditProjectEvents()
    object AddEditProject : AddEditProjectEvents()
}
