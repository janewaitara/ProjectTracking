package com.mumbicodes.projectie.domain.repository

import com.mumbicodes.projectie.data.helpers.LocalResult
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun getProjectById(projectId: Int): Project

    suspend fun getProjectByIdWithMilestones(projectId: Int): LocalResult <Flow<ProjectWithMilestones?>>

    suspend fun getAllProjects(projectOrder: ProjectsOrder): LocalResult<Flow<List<Project>>>

    suspend fun getProjectNameAndId(): LocalResult<Flow<List<ProjectName>>>

    suspend fun deleteProject(project: Project)

    suspend fun deleteAllProjects()
}
