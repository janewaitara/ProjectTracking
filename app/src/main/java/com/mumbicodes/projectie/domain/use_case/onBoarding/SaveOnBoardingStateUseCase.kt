package com.mumbicodes.projectie.domain.use_case.onBoarding

import com.mumbicodes.projectie.data.repository.OnBoardingDataStoreRepository

class SaveOnBoardingStateUseCase(
    private val onBoardingDataStoreRepository: OnBoardingDataStoreRepository,
) {
    suspend operator fun invoke(isOnBoarded: Boolean) {
        onBoardingDataStoreRepository.saveOnBoardingState(isOnBoarded)
    }
}
