package com.mumbicodes.domain.use_case.onBoarding

import com.mumbicodes.data.repository.OnBoardingDataStoreRepository

class SaveOnBoardingStateUseCase(
    private val onBoardingDataStoreRepository: OnBoardingDataStoreRepository,
) {
    suspend operator fun invoke(isOnBoarded: Boolean) {
        onBoardingDataStoreRepository.saveOnBoardingState(isOnBoarded)
    }
}
