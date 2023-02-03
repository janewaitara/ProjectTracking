package com.mumbicodes.projectie.data.repository

import com.mumbicodes.projectie.data.db.ProjectsDao
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class ProjectsRepositoryImpl(
    private val projectsDao: ProjectsDao,
) : ProjectsRepository {
    override suspend fun insertProject(project: Project) {
        projectsDao.insertProject(project = project)
    }

    override suspend fun updateProject(project: Project) {
        projectsDao.updateProject(project = project)
    }

    override suspend fun getProjectById(projectId: Int): Project =
        projectsDao.getProjectById(projectId)

    override fun getProjectByIdWithMilestones(projectId: Int): Flow<ProjectWithMilestones?> =
        projectsDao.getProjectByIdWithMilestones(projectId)

    override fun getAllProjects(): Flow<List<Project>> =
        projectsDao.getAllProjects()

    override fun getProjectNameAndId(): Flow<List<ProjectName>> =
        projectsDao.getProjectNameAndId()

    override suspend fun deleteProject(project: Project) {
        projectsDao.deleteProject(project)
    }

    override suspend fun deleteAllProjects() {
        projectsDao.deleteAllProjects()
    }
}
