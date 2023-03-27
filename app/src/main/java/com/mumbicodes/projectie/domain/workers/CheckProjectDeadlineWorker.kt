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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val today = LocalDate.now()

                val deadlineIsTodayProjects: MutableList<Project> = mutableListOf()

                CoroutineScope(Dispatchers.IO).launch {
                    allProjects.collectLatest { projects ->
                        projects.forEach {
                            if (it.projectDeadline.toLocalDate("dd MMM yyyy") == today) {
                                deadlineIsTodayProjects.add(it)
                                makeNotification(
                                    notificationType = NotificationType.PROJECTS,
                                    notificationId = it.projectId,
                                    message = "${it.projectName} deadline is today and it's ${it.projectStatus}",
                                    context = applicationContext,
                                )
                            }
                        }
                    }
                }

                // TODO make a notification
                // TODO group notifications
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
}
