package com.mumbicodes.domain.use_case.milestones

import android.app.Application
import com.mumbicodes.R
import com.mumbicodes.domain.model.Task
import com.mumbicodes.domain.util.ProgressStatus
import kotlinx.coroutines.flow.map

class CheckMilestoneStatusUseCase(
    private val appContext: Application,
) {

    operator fun invoke(tasks: List<Task>): ProgressStatus {

        val tasksStatusList: List<Boolean> = tasks.map { it.status }

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
