package com.mumbicodes.projectie.domain.use_case.milestones

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class GetAllMilestonesUseCase(private val repository: MilestonesRepository) {

    suspend operator fun invoke(): DataResult<Flow<List<MilestoneWithTasks>>> {
        return repository.getAllMilestones()
    }
}
