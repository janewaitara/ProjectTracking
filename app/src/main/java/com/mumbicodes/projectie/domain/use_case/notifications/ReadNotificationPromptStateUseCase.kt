package com.mumbicodes.projectie.domain.use_case.notifications

import com.mumbicodes.projectie.data.repository.NotificationPromptDataStoreRepository
import kotlinx.coroutines.flow.Flow

class ReadNotificationPromptStateUseCase(
    private val notificationPromptDataStoreRepository: NotificationPromptDataStoreRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        notificationPromptDataStoreRepository.readNotificationPromptState()
}
