package com.mumbicodes.projectie.domain.use_case.onBoarding

import com.mumbicodes.projectie.data.repository.OnBoardingDataStoreRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoardingStateUseCase(
    private val onBoardingDataStoreRepository: OnBoardingDataStoreRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        onBoardingDataStoreRepository.readOnBoardingState()
}
