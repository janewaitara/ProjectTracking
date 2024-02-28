package com.mumbicodes.projectie.data.repository

import com.mumbicodes.projectie.data.db.ProjectsDao
import com.mumbicodes.projectie.data.helpers.LocalResult
import com.mumbicodes.projectie.data.helpers.safeTransaction
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import com.mumbicodes.projectie.domain.repository.ProjectsRepository
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override suspend fun getProjectByIdWithMilestones(projectId: Int): LocalResult<Flow<ProjectWithMilestones?>> =
        safeTransaction { projectsDao.getProjectByIdWithMilestones(projectId) }

    /**
     * Added logic to get projects and sort the projects
     *
     * By default, the order is the date added
     * */
    override suspend fun getAllProjects(projectOrder: ProjectsOrder): LocalResult<Flow<List<Project>>> =
        safeTransaction {
            projectsDao.getAllProjects().map { projects ->
                when (projectOrder.orderType) {
                    is OrderType.Ascending -> {
                        when (projectOrder) {
                            is ProjectsOrder.Name -> projects.sortedBy { it.projectName.lowercase() }
                            is ProjectsOrder.Deadline -> projects.sortedBy { it.projectDeadline }
                            is ProjectsOrder.DateAdded -> projects.sortedBy { it.timeStamp }
                        }
                    }

                    is OrderType.Descending -> {
                        when (projectOrder) {
                            is ProjectsOrder.Name -> projects.sortedByDescending { it.projectName.lowercase() }
                            is ProjectsOrder.Deadline -> projects.sortedByDescending { it.projectDeadline }
                            is ProjectsOrder.DateAdded -> projects.sortedByDescending { it.timeStamp }
                        }
                    }
                }
            }
        }

    override suspend fun getProjectNameAndId(): LocalResult<Flow<List<ProjectName>>> =
        safeTransaction { projectsDao.getProjectNameAndId() }

    override suspend fun deleteProject(project: Project) {
        projectsDao.deleteProject(project)
    }

    override suspend fun deleteAllProjects() {
        projectsDao.deleteAllProjects()
    }
}
