package com.mumbicodes.projectie.domain.use_case.workers

import androidx.work.WorkInfo
import com.mumbicodes.projectie.domain.repository.WorkersRepository
import com.mumbicodes.projectie.domain.util.WorkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckWorkInfoStateUseCase(
    private val workersRepository: WorkersRepository,
) {
    operator fun invoke(): Flow<WorkerState> {
        val projectsWorkInfo = workersRepository.projectsWorkInfo.map { projectsWorkInfo ->
            when (projectsWorkInfo.state) {
                WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> WorkerState.STARTED
                else -> WorkerState.NOT_STARTED
            }
        }

        return projectsWorkInfo
    }
}
