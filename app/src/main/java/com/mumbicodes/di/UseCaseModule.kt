package com.mumbicodes.di

import android.app.Application
import com.mumbicodes.data.repository.OnBoardingDataStoreRepository
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.repository.ProjectsRepository
import com.mumbicodes.domain.repository.TasksRepository
import com.mumbicodes.domain.use_case.milestones.*
import com.mumbicodes.domain.use_case.onBoarding.OnBoardingUseCases
import com.mumbicodes.domain.use_case.onBoarding.ReadOnBoardingStateUseCase
import com.mumbicodes.domain.use_case.onBoarding.SaveOnBoardingStateUseCase
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
            updateProjectsUseCase = UpdateProjectsUseCase(projectRepository),
            getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository),
            getProjectByIdWithMilestonesUseCase = GetProjectByIdWithMilestonesUseCase(
                projectRepository
            ),
            getProjectsUseCase = GetProjectsUseCase(projectRepository),
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
            getMilestoneByIdWithTasksUseCase = GetMilestoneByIdWithTasksUseCase(repository),
            getMilestonesUseCase = GetMilestonesUseCase(repository),
            deleteMilestoneUseCase = DeleteMilestoneUseCase(repository),
            deleteMilestonesForProjectUseCase = DeleteMilestonesForProjectUseCase(repository),
            deleteAllMilestonesUseCase = DeleteAllMilestonesUseCase(repository),
            checkMilestoneStatusUseCase = CheckMilestoneStatusUseCase(app),
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

    @Provides
    @Singleton
    fun provideOnBoardingUseCases(repository: OnBoardingDataStoreRepository): OnBoardingUseCases {
        return OnBoardingUseCases(
            readOnBoardingStateUseCase = ReadOnBoardingStateUseCase(repository),
            saveOnBoardingStateUseCase = SaveOnBoardingStateUseCase(repository)
        )
    }
}
