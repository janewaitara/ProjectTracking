package com.mumbicodes.projectie.domain.use_case.workers

import com.mumbicodes.projectie.domain.repository.WorkersRepository

class CancelWorkerUseCase(
    private val workersRepository: WorkersRepository,
) {
    operator fun invoke() = workersRepository.cancelWork()
}
