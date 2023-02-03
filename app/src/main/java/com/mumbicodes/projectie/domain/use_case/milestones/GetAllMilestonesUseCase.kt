package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllMilestonesUseCase(private val repository: MilestonesRepository) {

    operator fun invoke(
        milestonesOrder: AllMilestonesOrder = AllMilestonesOrder.MostUrgent,
    ): Flow<List<MilestoneWithTasks>> {
        return repository.getAllMilestones().map { milestonesWithTasks ->
            when (milestonesOrder) {
                is AllMilestonesOrder.MostUrgent -> milestonesWithTasks.sortedBy {
                    it.milestone.milestoneEndDate
                }
                is AllMilestonesOrder.LeastUrgent -> milestonesWithTasks.sortedByDescending {
                    it.milestone.milestoneEndDate
                }
            }
        }
    }
}
