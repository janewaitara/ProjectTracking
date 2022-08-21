package com.mumbicodes.domain.use_case.milestones

import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.util.ProgressStatus

class CheckMilestoneStatusUseCase(private val repository: MilestonesRepository) {

    suspend operator fun invoke(milestoneId: Int): ProgressStatus {

        val milestone = repository.getMilestoneById(milestoneId)

        val tasksStatusList: List<Boolean> = milestone.tasks.map { it.status }

        return when {
            tasksStatusList.contains(true) && !tasksStatusList.contains(false) -> ProgressStatus.Completed("Completed")
            tasksStatusList.contains(false) && !tasksStatusList.contains(true) -> ProgressStatus.NotStarted("Not Started")
            else -> ProgressStatus.InProgress("In Progress")
        }
    }
}
