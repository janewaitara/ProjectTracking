package com.mumbicodes.projectie.domain.use_case.projects

import android.app.Application
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.util.ProgressStatus

class CheckProjectStatusUseCase(
    val repository: MilestonesRepository,
    private val appContext: Application,
) {
    suspend operator fun invoke(projectId: Int): DataResult<String> {

        val milestonesInProject =
            repository.getAllMilestonesBasedOnProjIdAndStatus(projectId = projectId, status = null)

        return when (milestonesInProject) {
            is DataResult.Error -> {
                DataResult.Error(errorMessage = milestonesInProject.errorMessage)
            }

            is DataResult.Success -> {
                val milestonesStatusList = milestonesInProject.data.map {
                    it.status
                }

                val completed =
                    milestonesStatusList.contains(appContext.getString(R.string.completed)) &&
                        !milestonesStatusList.contains(appContext.getString(R.string.notStarted)) &&
                        !milestonesStatusList.contains(appContext.getString(R.string.inProgress))

                val notStarted =
                    milestonesStatusList.contains(appContext.getString(R.string.notStarted)) &&
                        !milestonesStatusList.contains(appContext.getString(R.string.completed)) &&
                        !milestonesStatusList.contains(appContext.getString(R.string.inProgress))

                val progress = when {
                    completed -> ProgressStatus.Completed(appContext.getString(R.string.completed))
                    notStarted -> ProgressStatus.NotStarted(appContext.getString(R.string.notStarted))
                    else -> ProgressStatus.InProgress(appContext.getString(R.string.inProgress))
                }

                DataResult.Success(
                    when (progress) {
                        is ProgressStatus.Completed -> progress.status
                        is ProgressStatus.InProgress -> progress.status
                        is ProgressStatus.NotStarted -> progress.status
                    }
                )
            }
        }
    }
}
