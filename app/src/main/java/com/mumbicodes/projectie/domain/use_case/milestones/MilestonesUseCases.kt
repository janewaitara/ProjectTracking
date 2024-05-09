package com.mumbicodes.projectie.domain.use_case.milestones

data class MilestonesUseCases(
    val insertOrUpdateMilestoneUseCase: InsertOrUpdateMilestoneUseCase,
    val getMilestoneByIdWithTasksUseCase: GetMilestoneByIdWithTasksUseCase,
    val getMilestonesUseCase: GetMilestonesUseCase,
    val deleteMilestoneUseCase: DeleteMilestoneUseCase,
    val deleteMilestonesForProjectUseCase: DeleteMilestonesForProjectUseCase,
    val deleteAllMilestonesUseCase: DeleteAllMilestonesUseCase,
    val checkMilestoneStatusUseCase: CheckMilestoneStatusUseCase,
    val getAllMilestonesUseCase: GetAllMilestonesUseCase,
)
