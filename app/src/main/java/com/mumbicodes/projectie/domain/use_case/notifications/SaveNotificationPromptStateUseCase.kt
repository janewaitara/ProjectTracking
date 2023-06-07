package com.mumbicodes.projectie.domain.use_case.notifications

import com.mumbicodes.projectie.data.repository.NotificationPromptDataStoreRepository

class SaveNotificationPromptStateUseCase(
    private val notificationPromptDataStoreRepository: NotificationPromptDataStoreRepository,
) {
    suspend operator fun invoke(isPrompted: Boolean) {
        notificationPromptDataStoreRepository.saveNotificationPromptState(isPrompted)
    }
}
