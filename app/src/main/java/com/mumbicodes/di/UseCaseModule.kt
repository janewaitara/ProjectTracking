package com.mumbicodes.di

import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.repository.ProjectsRepository
import com.mumbicodes.domain.use_case.milestones.*
import com.mumbicodes.domain.use_case.projects.*
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
    fun provideProjectsUseCases(repository: ProjectsRepository): ProjectsUseCases {
        return ProjectsUseCases(
            addProjectsUseCase = AddProjectsUseCase(repository),
            getProjectByIdUseCase = GetProjectByIdUseCase(repository),
            getProjectsUseCase = GetProjectsUseCase(repository),
            deleteProjectUseCase = DeleteProjectUseCase(repository),
            deleteAllProjectsUseCase = DeleteAllProjectsUseCase(repository),
        )
    }

    @Provides
    @Singleton
    fun provideMilestonesUseCases(repository: MilestonesRepository): MilestonesUseCases {
        return MilestonesUseCases(
            addMilestoneUseCase = AddMilestoneUseCase(repository),
            getMilestoneByIdUseCase = GetMilestoneByIdUseCase(repository),
            getMilestonesUseCase = GetMilestonesUseCase(repository),
            deleteMilestoneUseCase = DeleteMilestoneUseCase(repository),
            deleteMilestonesForProjectUseCase = DeleteMilestonesForProjectUseCase(repository),
            deleteAllMilestonesUseCase = DeleteAllMilestonesUseCase(repository)
        )
    }
}
