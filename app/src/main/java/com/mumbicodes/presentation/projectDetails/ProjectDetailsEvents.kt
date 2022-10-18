package com.mumbicodes.presentation.projectDetails

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project

sealed class ProjectDetailsEvents {
    data class SelectedMilestonesStatus(val milestoneStatus: String) : ProjectDetailsEvents()
    data class GetMilestone(val milestoneId: Int) : ProjectDetailsEvents()
    data class DeleteProject(val project: Project) : ProjectDetailsEvents()
    data class DeleteMilestone(val milestone: Milestone) : ProjectDetailsEvents()
    object ToggleMenuOptionsVisibility : ProjectDetailsEvents()
    object ToggleDeleteDialogVisibility : ProjectDetailsEvents()
    object ToggleCongratulationsDialogVisibility : ProjectDetailsEvents()
}

// TODO the congrats dialog depends on whether all milestones are done
// Check status everytime a milestone is updated
sealed class ProjectUIEvents {
    data class ShowCongratsDialog(val message: String) : ProjectUIEvents()
    object DeleteProject : ProjectUIEvents()
}
