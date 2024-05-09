package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.domain.model.DataResult
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun getProjectById(projectId: Int): DataResult<Flow<Project>>

    suspend fun getProjectByIdWithMilestones(projectId: Int): DataResult <Flow<ProjectWithMilestones?>>

    suspend fun getAllProjects(): DataResult<Flow<List<Project>>>

    suspend fun getProjectNameAndId(): DataResult<Flow<List<ProjectName>>>

    suspend fun deleteProject(project: Project)

    suspend fun deleteAllProjects()
}
