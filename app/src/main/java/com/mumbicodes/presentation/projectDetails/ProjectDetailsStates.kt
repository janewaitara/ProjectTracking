package com.mumbicodes.presentation.projectDetails

import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.relations.MilestoneWithTasks
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
    val milestones: List<MilestoneWithTasks> = emptyList(),
    val mileStone: MilestoneWithTasks = MilestoneWithTasks(
        milestone = Milestone(
            projectId = 0,
            milestoneId = 0,
            milestoneTitle = "",
            milestoneSrtDate = 0,
            milestoneEndDate = 0,
            status = "",
        ),
        tasks = listOf()
    ),
    val selectedMilestoneStatus: String = filters.first(),
    val isMenuOptionsVisible: Boolean = false,
    val isCongratsDialogVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
)

// TODO research how I can replace the filters with an enum class
