package com.mumbicodes.projectie.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import com.mumbicodes.projectie.presentation.util.toLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

private const val TAG = "ProjectWorkerInTwoDays"

@HiltWorker
class CheckProjectDeadlineIsInTwoDaysWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val projectsRepository: ProjectsRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        delay(3000)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                Log.e("Reached 3", "It has been reached - 2 days ")

                val allProjects = projectsRepository.getAllProjects()

                CoroutineScope(Dispatchers.IO).launch {
                    allProjects.collectLatest { projects ->
                        val deadlineIsInTwoDaysProjects =
                            async { checkDeadlineIsInTwoDays(projects) }
                        deadlineIsInTwoDaysProjects.await().let { deadlineProjects ->
                            if (deadlineProjects.size > 1) {
                                makeNotification(
                                    notificationType = NotificationType.PROJECTS,
                                    notificationId = deadlineProjects.first().projectId,
                                    message = "You have ${deadlineProjects.size} projects ending in 2 days",
                                    context = applicationContext,
                                )
                            } else if (deadlineProjects.size == 1) {
                                makeNotification(
                                    notificationType = NotificationType.PROJECTS,
                                    notificationId = deadlineProjects.first().projectId,
                                    message = "${deadlineProjects.first().projectName} deadline is in 2 days and it's ${deadlineProjects.first().projectStatus}",
                                    context = applicationContext,
                                )
                            }
                        }
                    }
                }

                // TODO Research why when the outputData is not commented, the other worker is not reached
                // val outputData = workDataOf(KEY_ENDING_MILESTONES to deadlineIsTodayProjects)
                Result.success()
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

    private fun checkDeadlineIsInTwoDays(projects: List<Project>): MutableList<Project> {
        val deadlineInTwoDays = LocalDate.now().plusDays(2)
        val deadlineIsInTwoDaysProjects: MutableList<Project> = mutableListOf()

        projects.forEach {
            if (it.projectDeadline.toLocalDate("dd MMM yyyy") == deadlineInTwoDays) {
                deadlineIsInTwoDaysProjects.add(it)
            }
        }
        return deadlineIsInTwoDaysProjects
    }
}
