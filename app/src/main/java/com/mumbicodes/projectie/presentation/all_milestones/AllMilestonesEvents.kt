package com.mumbicodes.projectie.presentation.all_milestones

import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder

sealed class AllMilestonesEvents {
    object OrderMilestones : AllMilestonesEvents()

    data class UpdateMilestoneOrder(val milestonesOrder: AllMilestonesOrder) : AllMilestonesEvents()

    data class ResetMilestonesOrder(val milestonesOrder: AllMilestonesOrder) : AllMilestonesEvents()

    data class SelectMilestoneStatus(val milestoneStatus: String) : AllMilestonesEvents()

    data class SearchMilestone(val searchParam: String) : AllMilestonesEvents()

    data class DeleteMilestone(val milestone: Milestone) : AllMilestonesEvents()

    data class PassMilestone(val milestoneId: Int) : AllMilestonesEvents()

    data class ToggleTaskState(val taskId: Int) : AllMilestonesEvents()
}

sealed class AllMilestonesUIEvents {
    object DeleteMilestone : AllMilestonesUIEvents()
}
