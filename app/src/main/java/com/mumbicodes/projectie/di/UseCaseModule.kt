package com.mumbicodes.projectie.di

import android.app.Application
import com.mumbicodes.projectie.data.repository.NotificationPromptDataStoreRepository
import com.mumbicodes.projectie.data.repository.OnBoardingDataStoreRepository
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import com.mumbicodes.projectie.domain.repository.TasksRepository
import com.mumbicodes.projectie.domain.repository.WorkersRepository
import com.mumbicodes.projectie.domain.use_case.milestones.*
import com.mumbicodes.projectie.domain.use_case.notifications.NotificationUseCases
import com.mumbicodes.projectie.domain.use_case.notifications.ReadNotificationPromptStateUseCase
import com.mumbicodes.projectie.domain.use_case.notifications.SaveNotificationPromptStateUseCase
import com.mumbicodes.projectie.domain.use_case.onBoarding.OnBoardingUseCases
import com.mumbicodes.projectie.domain.use_case.onBoarding.ReadOnBoardingStateUseCase
import com.mumbicodes.projectie.domain.use_case.onBoarding.SaveOnBoardingStateUseCase
import com.mumbicodes.projectie.domain.use_case.projects.*
import com.mumbicodes.projectie.domain.use_case.tasks.DeleteTaskUseCase
import com.mumbicodes.projectie.domain.use_case.tasks.InsertOrUpdateTasksUseCase
import com.mumbicodes.projectie.domain.use_case.tasks.TasksUseCases
import com.mumbicodes.projectie.domain.use_case.tasks.TransformTasksUseCase
import com.mumbicodes.projectie.domain.use_case.workers.CancelWorkerUseCase
import com.mumbicodes.projectie.domain.use_case.workers.CheckDeadlinesUseCase
import com.mumbicodes.projectie.domain.use_case.workers.CheckWorkInfoStateUseCase
import com.mumbicodes.projectie.domain.use_case.workers.WorkersUseCases
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
            getProjectNameAndIdUseCase = GetProjectNameAndIdUseCase(projectRepository),
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
            getAllMilestonesUseCase = GetAllMilestonesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTasksUseCases(
        repository: TasksRepository,
    ): TasksUseCases {
        return TasksUseCases(
            insertOrUpdateTasksUseCase = InsertOrUpdateTasksUseCase(repository),
            deleteTaskUseCase = DeleteTaskUseCase(repository),
            transformTasksUseCase = TransformTasksUseCase(),
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

    @Provides
    @Singleton
    fun provideWorkersUseCases(repository: WorkersRepository): WorkersUseCases {
        return WorkersUseCases(
            checkDeadlinesUseCase = CheckDeadlinesUseCase(repository),
            cancelWorkerUseCase = CancelWorkerUseCase(repository),
            checkWorkInfoStateUseCase = CheckWorkInfoStateUseCase(repository),
        )
    }

    @Provides
    @Singleton
    fun provideNotificationUseCases(repository: NotificationPromptDataStoreRepository): NotificationUseCases {
        return NotificationUseCases(
            readNotificationPromptStateUseCase = ReadNotificationPromptStateUseCase(repository),
            saveNotificationPromptStateUseCase = SaveNotificationPromptStateUseCase(repository)
        )
    }
}
