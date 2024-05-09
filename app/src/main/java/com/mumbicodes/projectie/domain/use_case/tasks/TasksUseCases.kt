package com.mumbicodes.projectie.domain.use_case.tasks

class TasksUseCases(
    val insertOrUpdateTasksUseCase: InsertOrUpdateTasksUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val transformTasksUseCase: TransformTasksUseCase,
)
