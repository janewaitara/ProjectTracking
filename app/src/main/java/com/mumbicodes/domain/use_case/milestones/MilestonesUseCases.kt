package com.mumbicodes.domain.use_case.milestones

data class MilestonesUseCases(
    val addMilestoneUseCase: AddMilestoneUseCase,
    val getMilestoneByIdUseCase: GetMilestoneByIdUseCase,
    val getMilestonesUseCase: GetMilestonesUseCase,
    val deleteMilestoneUseCase: DeleteMilestoneUseCase,
    val deleteMilestonesForProjectUseCase: DeleteMilestonesForProjectUseCase,
    val deleteAllMilestonesUseCase: DeleteAllMilestonesUseCase
)
