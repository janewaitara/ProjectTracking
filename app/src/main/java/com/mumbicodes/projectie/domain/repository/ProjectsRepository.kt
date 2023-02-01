package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
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
