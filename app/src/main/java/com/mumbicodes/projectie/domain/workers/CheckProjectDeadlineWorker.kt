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

private const val TAG = "ProjectWorkerIsToday"

@HiltWorker
class CheckProjectDeadlineWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val projectsRepository: ProjectsRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val allProjects = projectsRepository.getAllProjects()

                CoroutineScope(Dispatchers.IO).launch {
                    allProjects.collectLatest { projects ->
                        val deadlineIsTodayProjects = async { checkDeadlineIsToday(projects) }
                        deadlineIsTodayProjects.await().let { deadlineProjects ->
                            if (deadlineProjects.isNotEmpty() && deadlineProjects.size > 1) {
                                // Grouped Notifications
                                makeNotification(
                                    notificationType = NotificationType.PROJECTS,
                                    notificationId = deadlineProjects.first().projectId,
                                    message = "You have ${deadlineProjects.size} projects ending Today",
                                    context = applicationContext,
                                )
                            } else if (deadlineProjects.size == 1) {
                                // One notification
                                makeNotification(
                                    notificationType = NotificationType.PROJECTS,
                                    notificationId = deadlineProjects.first().projectId,
                                    message = "${deadlineProjects.first().projectName} deadline is today and it's ${deadlineProjects.first().projectStatus}",
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

    private fun checkDeadlineIsToday(projects: List<Project>): MutableList<Project> {
        val today = LocalDate.now()
        val deadlineIsTodayProjects: MutableList<Project> = mutableListOf()

        projects.forEach {
            if (it.projectDeadline.toLocalDate("dd MMM yyyy") == today) {
                deadlineIsTodayProjects.add(it)
            }
        }
        return deadlineIsTodayProjects
    }
}
