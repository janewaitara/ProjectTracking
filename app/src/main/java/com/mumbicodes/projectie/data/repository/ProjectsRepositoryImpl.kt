package com.mumbicodes.projectie.data.repository

import com.mumbicodes.projectie.data.db.ProjectsDao
import com.mumbicodes.projectie.data.helpers.LocalResult
import com.mumbicodes.projectie.data.helpers.safeTransaction
import com.mumbicodes.projectie.data.helpers.toDataResult
import com.mumbicodes.projectie.domain.model.DataResult
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

    override suspend fun getProjectById(projectId: Int): DataResult<Flow<Project>> =
        when (val localResult = safeTransaction { projectsDao.getProjectById(projectId) }) {
            is LocalResult.Error -> DataResult.Error(localResult.errorMessage)
            is LocalResult.Success -> DataResult.Success(localResult.data)
        }

    override suspend fun getProjectByIdWithMilestones(projectId: Int): DataResult<Flow<ProjectWithMilestones?>> =
        when (
            val localResult =
                safeTransaction { projectsDao.getProjectByIdWithMilestones(projectId) }
        ) {
            is LocalResult.Error -> DataResult.Error(localResult.errorMessage)
            is LocalResult.Success -> DataResult.Success(localResult.data)
        }

    /**
     * Added logic to get projects and sort the projects
     *
     * By default, the order is the date added
     * */
    override suspend fun getAllProjects(projectOrder: ProjectsOrder): DataResult<Flow<List<Project>>> =
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
        }.toDataResult()

    override suspend fun getProjectNameAndId(): DataResult<Flow<List<ProjectName>>> =
        safeTransaction { projectsDao.getProjectNameAndId() }.toDataResult()

    override suspend fun deleteProject(project: Project) {
        projectsDao.deleteProject(project)
    }

    override suspend fun deleteAllProjects() {
        projectsDao.deleteAllProjects()
    }
}
