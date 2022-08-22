package com.mumbicodes.domain.use_case.projects

import android.app.Application
import com.mumbicodes.R
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.repository.ProjectsRepository
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProjectsUseCase(
    private val repository: ProjectsRepository,
    private val appContext: Application,
) {
    /**
     * Added logic to get projects and sort the projects
     *
     * By default, the order is the date added
     * */
    operator fun invoke(
        projectStatus: String,
        projectOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    ): Flow<List<Project>> {

        // if the project status selected is all, pass null to room
        val validProjectStatus = when (projectStatus) {
            appContext.getString(R.string.all) -> null
            else -> projectStatus
        }

        return repository.getAllProjectsBasedOnStatus(validProjectStatus)
            .map { projects ->
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
}
