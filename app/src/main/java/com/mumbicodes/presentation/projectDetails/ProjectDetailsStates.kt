package com.mumbicodes.presentation.projectDetails

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project
import com.mumbicodes.presentation.allProjects.filters

data class ProjectDetailsStates(
    val project: Project = Project(
        projectId = 0,
        projectName = "",
        projectDesc = "",
        projectDeadline = "",
        projectStatus = "",
        timeStamp = 1
    ),
    val milestones: List<Milestone> = emptyList(),
    val mileStone: Milestone = milestones.first(),
    val selectedMilestoneStatus: String = filters.first(),
    val isMenuOptionsVisible: Boolean = false,
    val isCongratsDialogVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
)

// TODO research how I can replace the filters with an enum class
