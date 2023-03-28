package com.mumbicodes.projectie.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.repository.WorkersRepository
import com.mumbicodes.projectie.presentation.util.toLong
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

private const val TAG = "MilestonesWorker"
@HiltWorker
class CheckMilestoneDeadlineWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val milestonesRepository: MilestonesRepository,
    private val workersRepository: WorkersRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        // Without this, the notification is shown immediately after the projects ones, blocking them
        delay(3000)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                Log.e("Reached 2", "It has been reached ")
                val allMilestones = milestonesRepository.getAllMilestones()
                val today = LocalDate.now()

                val filteredMilestones: MutableList<MilestoneWithTasks> = mutableListOf()

                CoroutineScope(Dispatchers.IO).launch {
                    allMilestones.collectLatest { milestonesWithTasks ->
                        milestonesWithTasks.forEach {
                            if (it.milestone.milestoneEndDate == today.toLong()) {
                                filteredMilestones.add(it)
                                makeNotification(
                                    NotificationType.MILESTONES,
                                    it.milestone.milestoneId,
                                    "${it.milestone.milestoneTitle} deadline is today and it's ${it.milestone.status}",
                                    applicationContext,
                                )
                            }
                        }
                    }
                }
                // val outputData = workDataOf(KEY_ENDING_MILESTONES to filteredMilestones)
                workersRepository.checkDeadlines()
                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_checking_milestone_Deadline),
                    throwable
                )
                Result.failure()
            }
        }
    }
}
