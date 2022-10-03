package com.mumbicodes.data.repository

import com.mumbicodes.data.db.ProjectsDao
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.relations.ProjectWithMilestones
import com.mumbicodes.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class ProjectsRepositoryImpl(
    private val projectsDao: ProjectsDao
) : ProjectsRepository {
    override suspend fun insertProject(project: Project) {
        projectsDao.insertProject(project = project)
    }

    override suspend fun getProjectById(projectId: Int): Project =
        projectsDao.getProjectById(projectId)

    override fun getProjectByIdWithMilestones(projectId: Int): Flow<ProjectWithMilestones> =
        projectsDao.getProjectByIdWithMilestones(projectId)

    override fun getAllProjectsBasedOnStatus(projectStatus: String?): Flow<List<Project>> =
        projectsDao.getAllProjectsBasedOnStatus(projectStatus)

    override suspend fun deleteProject(project: Project) {
        projectsDao.deleteProject(project)
    }

    override suspend fun deleteAllProjects() {
        projectsDao.deleteAllProjects()
    }
}
