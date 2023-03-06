package com.mumbicodes.projectie.domain.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.presentation.util.KEY_ENDING_MILESTONES
import com.mumbicodes.projectie.presentation.util.toLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.time.LocalDate

private const val TAG = "MilestonesWorker"

class CheckMilestoneDeadlineWorker(
    appContext: Context,
    params: WorkerParameters,
    private val milestonesRepository: MilestonesRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val allMilestones = milestonesRepository.getAllMilestones()
                val today = LocalDate.now()

                val filteredMilestones: MutableList<MilestoneWithTasks> = mutableListOf()

                allMilestones.collectLatest { milestonesWithTasks ->
                    milestonesWithTasks.forEach {
                        if (it.milestone.milestoneEndDate == today.toLong()) {
                            filteredMilestones.add(it)
                        }
                    }
                }
                val outputData = workDataOf(KEY_ENDING_MILESTONES to filteredMilestones)
                Result.success(outputData)
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
