package com.mumbicodes.di

import android.content.Context
import androidx.room.Room
import com.mumbicodes.data.db.ProjectsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideProjectsDatabase(@ApplicationContext context: Context): ProjectsDatabase {
        return Room.databaseBuilder(
            context,
            ProjectsDatabase::class.java,
            DATABASE_NAME
        )
            // .addTypeConverter(TaskConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    private const val DATABASE_NAME = "projects_db"
}
