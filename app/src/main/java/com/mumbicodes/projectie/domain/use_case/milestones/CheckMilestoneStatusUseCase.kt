package com.mumbicodes.projectie.domain.use_case.milestones

import android.app.Application
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.util.ProgressStatus

class CheckMilestoneStatusUseCase(
    private val appContext: Application,
) {

    operator fun invoke(tasks: List<Task>): String {

        val tasksStatusList: List<Boolean> = tasks.map { it.status }

        val progress = when {
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

        // return milestoneStatus
        return when (progress) {
            is ProgressStatus.Completed -> progress.status
            is ProgressStatus.InProgress -> progress.status
            is ProgressStatus.NotStarted -> progress.status
        }
    }
}
