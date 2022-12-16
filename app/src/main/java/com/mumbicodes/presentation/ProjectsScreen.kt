package com.mumbicodes.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mumbicodes.presentation.allProjects.AllProjectsScreen
import com.mumbicodes.presentation.projectDetails.ProjectDetailsScreen
import com.mumbicodes.presentation.util.ContentType
import com.mumbicodes.presentation.util.navigation.Screens

@Composable
fun ProjectsScreen(
    contentType: ContentType,
    onClickProject: (Int) -> Unit,
    navController: NavHostController,
    projectId: MutableState<Int> = rememberSaveable { mutableStateOf(-1) },
) {

    if (contentType == ContentType.DUAL_PANE) {
        ProjectsListDetailComposable(
            projectId = projectId,
            navController = navController
        )
    } else {
        AllProjectsScreen(onClickProject = onClickProject)
    }
}

@Composable
fun ProjectsListDetailComposable(
    projectId: MutableState<Int> = rememberSaveable { mutableStateOf(-1) },
    navController: NavHostController,
) {

    Row(modifier = Modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            AllProjectsScreen(
                onClickProject = { projectIdSelected ->
                    projectId.value = projectIdSelected
                }
            )
        }

        // TODO find a way to pass the project ID without navigating to a new screen
        if (projectId.value != -1) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                ProjectDetailsScreen(
                    onEditProject = { projectId ->
                        navController.navigate(
                            "${Screens.AddAndEditScreens.route}/$projectId"
                        )
                    },
                    onAddOrModifyMilestone = { projectId, milestoneId ->
                        navController.navigate(
                            "${Screens.AddAndEditMilestoneScreen.route}/$projectId/$milestoneId"
                        )
                    },
                    onClickIconBack = {
                        navController.popBackStack()
                    },
                    navigateToAllProjects = {
                        navController.navigateUp()
                    },
                    projectId = projectId.value
                )
            }
        }
    }
}
