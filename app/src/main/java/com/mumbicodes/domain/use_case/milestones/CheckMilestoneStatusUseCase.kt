package com.mumbicodes.domain.use_case.milestones

import android.app.Application
import com.mumbicodes.R
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.util.ProgressStatus

class CheckMilestoneStatusUseCase(
    private val repository: MilestonesRepository,
    private val appContext: Application,
) {

    suspend operator fun invoke(milestoneId: Int): ProgressStatus {

        val milestone = repository.getMilestoneById(milestoneId)

        val tasksStatusList: List<Boolean> = milestone.tasks.map { it.status }

        return when {
            tasksStatusList.contains(true) && !tasksStatusList.contains(false) -> ProgressStatus.Completed(
                appContext.getString(
                    R.string.completed
                )
            )
            tasksStatusList.contains(false) && !tasksStatusList.contains(true) -> ProgressStatus.NotStarted(
                appContext.getString(R.string.notStarted)
            )
            else -> ProgressStatus.InProgress(appContext.getString(R.string.inProgress))
        }
    }
}
