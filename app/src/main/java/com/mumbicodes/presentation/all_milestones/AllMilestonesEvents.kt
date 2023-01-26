package com.mumbicodes.presentation.all_milestones

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.util.AllMilestonesOrder

sealed class AllMilestonesEvents {
    data class OrderMilestones(val milestonesOrder: AllMilestonesOrder) : AllMilestonesEvents()

    data class ResetMilestonesOrder(val milestonesOrder: AllMilestonesOrder) : AllMilestonesEvents()

    data class SelectMilestoneStatus(val milestoneStatus: String) : AllMilestonesEvents()

    data class SearchMilestone(val searchParam: String) : AllMilestonesEvents()

    data class DeleteMilestone(val milestone: Milestone) : AllMilestonesEvents()
}

sealed class AllMilestonesUIEvents {
    object DeleteMilestone : AllMilestonesUIEvents()
}
