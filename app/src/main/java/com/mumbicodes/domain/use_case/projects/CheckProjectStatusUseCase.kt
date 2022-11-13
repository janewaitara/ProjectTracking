package com.mumbicodes.domain.use_case.projects

import android.app.Application
import android.util.Log
import com.mumbicodes.R
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.util.ProgressStatus

class CheckProjectStatusUseCase(
    val repository: MilestonesRepository,
    private val appContext: Application,
) {

    suspend operator fun invoke(projectId: Int): ProgressStatus {

        Log.e("Mini me ", "Wierd, right")
        val milestonesInProject =
            repository.getAllMilestonesBasedOnProjIdAndStatus(projectId = projectId, status = null)

        Log.e("Mini me x ", ".col")
        var milestonesStatusList = listOf<String>()

        Log.e("Mini me y", "${milestonesStatusList.size}")

        milestonesInProject.collect { milestones ->
            Log.e("Mini me z1", "milestones: ${milestones.size}")
            milestonesStatusList = milestones.map { it.status }
            Log.e("Mini me z2", "milestones 2: ${milestonesStatusList.size}")
        }

        Log.e("Mini me z3", "${milestonesStatusList.size}")

        val completed = milestonesStatusList.contains(appContext.getString(R.string.completed)) &&
            !milestonesStatusList.contains(appContext.getString(R.string.notStarted)) &&
            !milestonesStatusList.contains(appContext.getString(R.string.inProgress))
        Log.e("Mini me zl", "${milestonesStatusList.size}")

        val notStarted = milestonesStatusList.contains(appContext.getString(R.string.notStarted)) &&
            !milestonesStatusList.contains(appContext.getString(R.string.completed)) &&
            !milestonesStatusList.contains(appContext.getString(R.string.inProgress))

        Log.e("Mini me zl 2", "$completed")

        return when {
            completed -> ProgressStatus.Completed(appContext.getString(R.string.completed))
            notStarted -> ProgressStatus.NotStarted(appContext.getString(R.string.notStarted))
            else -> ProgressStatus.InProgress(appContext.getString(R.string.inProgress))
        }
    }
}
