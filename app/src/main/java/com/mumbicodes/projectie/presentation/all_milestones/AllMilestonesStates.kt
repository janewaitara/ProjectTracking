package com.mumbicodes.projectie.presentation.all_milestones

import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder
import com.mumbicodes.projectie.presentation.allProjects.filters

data class AllMilestonesStates(
    val milestones: List<MilestoneWithTasks> = emptyList(),
    val filteredMilestones: List<MilestoneWithTasks> = emptyList(),
    val mileStone: MilestoneWithTasks = MilestoneWithTasks(
        milestone = Milestone(
            projectId = 0,
            milestoneId = 0,
            milestoneTitle = "",
            milestoneSrtDate = 0,
            milestoneEndDate = 0,
            status = "",
        ),
        tasks = listOf()
    ),
    val milestonesOrder: AllMilestonesOrder = AllMilestonesOrder.MostUrgent,
    val filtersStatus: List<String> = filters,
    val selectedMilestoneStatus: String = filters.first(),
    val isFilterBottomSheetVisible: Boolean = false,
    val milestonesProjectName: Map<Int, String> = emptyMap(),
)
