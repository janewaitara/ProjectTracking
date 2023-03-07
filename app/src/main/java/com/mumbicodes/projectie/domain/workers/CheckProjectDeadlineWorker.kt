package com.mumbicodes.projectie.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import com.mumbicodes.projectie.presentation.util.KEY_ENDING_MILESTONES
import com.mumbicodes.projectie.presentation.util.toLocalDate
import dagger.assisted.Assisted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.time.LocalDate

private const val TAG = "ProjectWorker"

@HiltWorker
class CheckProjectDeadlineWorker(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val projectsRepository: ProjectsRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val allProjects = projectsRepository.getAllProjects()
                val deadlineInTwoDays = LocalDate.now().plusDays(2)
                val today = LocalDate.now()

                val deadlineIsInTwoDaysProjects: MutableList<Project> = mutableListOf()
                val deadlineIsTodayProjects: MutableList<Project> = mutableListOf()

                allProjects.collectLatest { projects ->
                    projects.forEach {
                        if (it.projectDeadline.toLocalDate("dd MMM yyyy") == deadlineInTwoDays) {
                            deadlineIsInTwoDaysProjects.add(it)
                        } else if (it.projectDeadline.toLocalDate("dd MMM yyyy") == today) {
                            deadlineIsTodayProjects.add(it)
                        }
                    }
                }

                // TODO make a notification
                val outputData = workDataOf(KEY_ENDING_MILESTONES to deadlineIsTodayProjects)
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_checking_project_Deadline),
                    throwable
                )
                Result.failure()
            }
        }
    }
}
