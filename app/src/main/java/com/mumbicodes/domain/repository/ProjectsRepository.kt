package com.mumbicodes.domain.repository

import com.mumbicodes.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {

    suspend fun insertProject(project: Project)

    suspend fun getProjectById(projectId: Int): Project

    fun getAllProjectsBasedOnStatus(projectStatus: String?): Flow<List<Project>>

    fun getProjectsBasedOnDueDate(projectStatus: String?): Flow<List<Project>>

    suspend fun deleteProject(project: Project)

    suspend fun deleteAllProjects()
}
