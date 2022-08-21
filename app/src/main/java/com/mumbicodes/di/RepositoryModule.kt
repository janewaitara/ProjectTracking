package com.mumbicodes.di

import com.mumbicodes.data.db.ProjectsDatabase
import com.mumbicodes.data.repository.MilestonesRepositoryImpl
import com.mumbicodes.data.repository.ProjectsRepositoryImpl
import com.mumbicodes.domain.repository.MilestonesRepository
import com.mumbicodes.domain.repository.ProjectsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}
