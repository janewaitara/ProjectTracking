package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder
import kotlinx.coroutines.flow.Flow

class GetAllMilestonesUseCase(private val repository: MilestonesRepository) {

    suspend operator fun invoke(
        milestonesOrder: AllMilestonesOrder = AllMilestonesOrder.MostUrgent,
    ): DataResult<Flow<List<MilestoneWithTasks>>> {
        return repository.getAllMilestones(milestonesOrder)
    }
}
