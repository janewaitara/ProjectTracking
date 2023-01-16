package com.mumbicodes.domain.use_case.onBoarding

import com.mumbicodes.data.repository.OnBoardingDataStoreRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoardingStateUseCase(
    private val onBoardingDataStoreRepository: OnBoardingDataStoreRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        onBoardingDataStoreRepository.readOnBoardingState()
}
