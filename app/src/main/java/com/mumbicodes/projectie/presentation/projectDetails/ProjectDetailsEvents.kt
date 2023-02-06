package com.mumbicodes.projectie.presentation.projectDetails

import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Project

sealed class ProjectDetailsEvents {
    data class SelectedMilestonesStatus(val milestoneStatus: String) : ProjectDetailsEvents()
    data class GetMilestone(val milestoneId: Int) : ProjectDetailsEvents()
    data class DeleteProject(val project: Project) : ProjectDetailsEvents()
    data class DeleteMilestone(val milestone: Milestone) : ProjectDetailsEvents()
    object ToggleMenuOptionsVisibility : ProjectDetailsEvents()
    data class ToggleTaskState(val taskId: Int) : ProjectDetailsEvents()
    object ToggleDeleteDialogVisibility : ProjectDetailsEvents()
    object ToggleCongratulationsDialogVisibility : ProjectDetailsEvents()
}

// TODO the congrats dialog depends on whether all milestones are done
// Check status everytime a milestone is updated
sealed class ProjectUIEvents {
    data class ShowCongratsDialog(val message: String) : ProjectUIEvents()
    object DeleteProject : ProjectUIEvents()
    object DeleteMilestone : ProjectUIEvents()
}
