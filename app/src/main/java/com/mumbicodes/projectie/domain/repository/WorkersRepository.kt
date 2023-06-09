package com.mumbicodes.projectie.domain.repository

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface WorkersRepository {

    val projectsWorkInfo: Flow<WorkInfo>
    fun checkDeadlines()

    fun cancelWork()
}
