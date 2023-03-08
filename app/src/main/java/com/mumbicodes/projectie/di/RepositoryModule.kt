package com.mumbicodes.projectie.di

import android.content.Context
import com.mumbicodes.projectie.data.db.ProjectsDatabase
import com.mumbicodes.projectie.data.repository.*
import com.mumbicodes.projectie.domain.repository.MilestonesRepository
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import com.mumbicodes.projectie.domain.repository.TasksRepository
import com.mumbicodes.projectie.domain.repository.WorkersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProjectsRepository(db: ProjectsDatabase): ProjectsRepository {
        return ProjectsRepositoryImpl(db.projectsDao())
    }

    @Provides
    @Singleton
    fun provideMilestonesRepository(db: ProjectsDatabase): MilestonesRepository {
        return MilestonesRepositoryImpl(db.milestonesDao())
    }

    @Provides
    @Singleton
    fun provideTasksRepository(db: ProjectsDatabase): TasksRepository {
        return TaskRepositoryImpl(db.tasksDao())
    }

    @Provides
    @Singleton
    fun provideOnBoardingDataStoreRepository(@ApplicationContext context: Context) =
        OnBoardingDataStoreRepository(context = context)

    @Provides
    @Singleton
    fun provideWorkersRepository(@ApplicationContext context: Context): WorkersRepository =
        WorkersRepositoryImpl(context = context)
}
