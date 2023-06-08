package com.mumbicodes.projectie.data.repository

import android.content.Context
import androidx.work.*
import com.mumbicodes.projectie.domain.repository.WorkersRepository
import com.mumbicodes.projectie.domain.workers.CheckMilestoneDeadlineWorker
import com.mumbicodes.projectie.domain.workers.CheckProjectDeadlineIsInTwoDaysWorker
import com.mumbicodes.projectie.domain.workers.CheckProjectDeadlineWorker
import com.mumbicodes.projectie.presentation.util.PROJECTS_DEADLINE_WORK_NAME
import com.mumbicodes.projectie.presentation.util.PROJECTS_WORKER_TAG
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class WorkersRepositoryImpl(context: Context) : WorkersRepository {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Create the WorkRequests to check for projects and milestones deadlines and show a notification
     */
    override fun checkDeadlines() {

        val alarmTime = LocalTime.of(8, 30) // trigger at 8:30am
        var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        val nowTime = now.toLocalTime()

        if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
            now = now.plusDays(1)
        }
        now = now.withHour(alarmTime.hour).withMinute(alarmTime.minute)
        val duration = Duration.between(LocalDateTime.now(), now)

        val projectsWorker = OneTimeWorkRequestBuilder<CheckProjectDeadlineWorker>()
            .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
            .addTag(PROJECTS_WORKER_TAG)
            .build()
        val projectsDeadlineInTwoDaysWorker =
            OneTimeWorkRequestBuilder<CheckProjectDeadlineIsInTwoDaysWorker>()
                .build()
        val milestonesWorker = OneTimeWorkRequestBuilder<CheckMilestoneDeadlineWorker>()
            .build()

        val continuation = workManager.beginUniqueWork(
            PROJECTS_DEADLINE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            projectsWorker
        ).then(projectsDeadlineInTwoDaysWorker)
            .then(milestonesWorker)

        continuation.enqueue()
    }

    /**
     * Cancel any ongoing WorkRequests
     * */
    override fun cancelWork() {
        workManager.cancelUniqueWork(PROJECTS_DEADLINE_WORK_NAME)
    }
}
