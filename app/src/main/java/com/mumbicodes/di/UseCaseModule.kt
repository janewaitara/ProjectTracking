package com.mumbicodes.di

import android.app.Application
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.repository.ProjectsRepository
import com.mumbicodes.domain.repository.TasksRepository
import com.mumbicodes.domain.use_case.milestones.*
import com.mumbicodes.domain.use_case.projects.*
import com.mumbicodes.domain.use_case.tasks.AddTasksUseCase
import com.mumbicodes.domain.use_case.tasks.DeleteTaskUseCase
import com.mumbicodes.domain.use_case.tasks.TasksUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideProjectsUseCases(
        projectRepository: ProjectsRepository,
        milestonesRepository: MilestonesRepository,
        app: Application,
    ): ProjectsUseCases {
        return ProjectsUseCases(
            addProjectsUseCase = AddProjectsUseCase(projectRepository),
            getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository),
            getProjectByIdWithMilestonesUseCase = GetProjectByIdWithMilestonesUseCase(
                projectRepository
            ),
            getProjectsUseCase = GetProjectsUseCase(projectRepository, app),
            deleteProjectUseCase = DeleteProjectUseCase(projectRepository),
            deleteAllProjectsUseCase = DeleteAllProjectsUseCase(projectRepository),
            checkProjectStatusUseCase = CheckProjectStatusUseCase(milestonesRepository, app),
        )
    }

    @Provides
    @Singleton
    fun provideMilestonesUseCases(
        repository: MilestonesRepository,
        app: Application,
    ): MilestonesUseCases {
        return MilestonesUseCases(
            addMilestoneUseCase = AddMilestoneUseCase(repository),
            getMilestoneByIdUseCase = GetMilestoneByIdUseCase(repository),
            getMilestonesUseCase = GetMilestonesUseCase(repository),
            deleteMilestoneUseCase = DeleteMilestoneUseCase(repository),
            deleteMilestonesForProjectUseCase = DeleteMilestonesForProjectUseCase(repository),
            deleteAllMilestonesUseCase = DeleteAllMilestonesUseCase(repository),
            checkMilestoneStatusUseCase = CheckMilestoneStatusUseCase(repository, app),
        )
    }

    @Provides
    @Singleton
    fun provideTasksUseCases(
        repository: TasksRepository,
    ): TasksUseCases {
        return TasksUseCases(
            addTasksUseCase = AddTasksUseCase(repository),
            deleteTaskUseCase = DeleteTaskUseCase(repository)
        )
    }
}
