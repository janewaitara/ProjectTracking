package com.mumbicodes.domain.use_case.milestones

import android.app.Application
import com.mumbicodes.R
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.util.ProgressStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class CheckMilestoneStatusUseCase(
    private val repository: MilestonesRepository,
    private val appContext: Application,
) {

    suspend operator fun invoke(milestoneId: Int): ProgressStatus {

        val milestoneWithTasks = repository.getMilestoneByIdWithTasks(milestoneId)

        var tasksStatusList: List<Boolean> = listOf()
        milestoneWithTasks.collectLatest { milestoneTasks ->
            tasksStatusList = milestoneTasks.tasks.map { it.status }
        }

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
