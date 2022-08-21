package com.mumbicodes.domain.use_case.projects

import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.util.ProgressStatus
import kotlinx.coroutines.flow.collectLatest

class CheckProjectStatusUseCase(val repository: MilestonesRepository) {

    suspend operator fun invoke(projectId: Int): ProgressStatus {

        val milestonesInProject =
            repository.getAllMilestonesBasedOnProjIdAndStatus(projectId = projectId, status = null)

        var milestonesStatusList = listOf<String>()

        milestonesInProject.collectLatest { milestones ->
            milestonesStatusList = milestones.map { it.status }
        }

        val completed = milestonesStatusList.contains("Completed") &&
            !milestonesStatusList.contains("Not Started") &&
            !milestonesStatusList.contains("In Progress")

        val notStarted = milestonesStatusList.contains("Not Started") &&
            !milestonesStatusList.contains("Completed") &&
            !milestonesStatusList.contains("In Progress")

        return when {
            completed -> ProgressStatus.Completed("Completed")
            notStarted -> ProgressStatus.NotStarted("Not Started")
            else -> ProgressStatus.InProgress("In Progress")
        }
    }
}
