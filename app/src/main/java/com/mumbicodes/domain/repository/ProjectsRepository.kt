package com.mumbicodes.domain.repository

import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.model.ProjectName
import com.mumbicodes.domain.relations.ProjectWithMilestones
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun getProjectById(projectId: Int): Project

    fun getProjectByIdWithMilestones(projectId: Int): Flow<ProjectWithMilestones?>

    fun getAllProjects(): Flow<List<Project>>

    fun getProjectNameAndId(): Flow<List<ProjectName>>

    suspend fun deleteProject(project: Project)

    suspend fun deleteAllProjects()
}
