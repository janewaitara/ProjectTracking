package com.mumbicodes.projectie.presentation.screens.all_milestones

import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder
import com.mumbicodes.projectie.presentation.screens.allProjects.filters
import com.mumbicodes.projectie.presentation.util.state.ListState
import com.mumbicodes.projectie.presentation.util.state.SuccessState

/**
 * @param selectedMilestoneOrder Holds the user selection until they press filter - important to show user selection on radios
 * */
data class AllMilestonesStates(
    val milestones: ListState<List<MilestoneWithTasks>> = ListState.Loading,
    val mileStone: MilestoneWithTasks? = null,
    val milestonesOrder: AllMilestonesOrder = AllMilestonesOrder.MostUrgent,
    val filtersStatus: List<String> = filters,
    val selectedMilestoneStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
    val milestonesProjectName: Map<Int, String> = emptyMap(),
    val searchParam: String = "",
    val selectedMilestoneOrder: AllMilestonesOrder = milestonesOrder,
) {
    val filteredMilestones: ListState<List<MilestoneWithTasks>>
        get() = if (milestones is ListState.Success) {
            if (milestones.data is SuccessState.Data) {
                val allMilestones = milestones.data.data

                val filteredList = allMilestones
                    .filter {
                        if (selectedMilestoneStatus == filters.first()) true
                        else it.milestone.status == selectedMilestoneStatus
                    }.filter {
                        it.milestone.milestoneTitle.contains(searchParam)
                    }.let { milestonesWithTasks ->
                        when (milestonesOrder) {
                            is AllMilestonesOrder.MostUrgent -> milestonesWithTasks.sortedBy {
                                it.milestone.milestoneEndDate
                            }

                            is AllMilestonesOrder.LeastUrgent -> milestonesWithTasks.sortedByDescending {
                                it.milestone.milestoneEndDate
                            }
                        }
                    }

                if (filteredList.isEmpty()) ListState.Success(SuccessState.Empty)
                else ListState.Success(SuccessState.Data(filteredList))
            } else {
                milestones
            }
        } else {
            milestones
        }
}
