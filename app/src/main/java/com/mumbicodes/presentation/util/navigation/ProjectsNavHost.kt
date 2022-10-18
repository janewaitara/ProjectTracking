package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mumbicodes.presentation.add_edit_milestone.AddAndEditMilestoneScreen
import com.mumbicodes.presentation.add_edit_project.AddAndEditScreen
import com.mumbicodes.presentation.allProjects.AllProjectsScreen
import com.mumbicodes.presentation.projectDetails.ProjectDetailsScreen

@Composable
fun ProjectNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isBottomBarVisible: (Boolean) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.AllProjectsScreens.route,
    ) {

        composable(route = Screens.AllProjectsScreens.route) {
            isBottomBarVisible(true)
            AllProjectsScreen(onClickProject = { projectId ->
                navController.navigate(
                    /*"${Screens.AddAndEditScreens.route}/$projectId"*/
                    "${Screens.ProjectDetails.route}/$projectId"
                )
            })
        }
        composable(
            route = Screens.AddAndEditScreens.routeWithArgs,
            arguments = Screens.AddAndEditScreens.arguments
        ) {
            isBottomBarVisible(false)
            AddAndEditScreen(navController = navController)
        }

        composable(
            route = Screens.AddAndEditMilestoneScreen.routeWithArgs,
            arguments = Screens.AddAndEditMilestoneScreen.arguments
        ) {
            isBottomBarVisible(false)
            AddAndEditMilestoneScreen(
                onAddEditMilestone = { navController.navigateUp() },
                onClickIconBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screens.ProjectDetails.routeWithArgs,
            arguments = Screens.ProjectDetails.arguments
        ) {
            isBottomBarVisible(false)
            ProjectDetailsScreen(
                onEditProject = { projectId ->
                    navController.navigate(
                        "${Screens.AddAndEditScreens.route}/$projectId"
                    )
                },
                onAddOrModifyMilestone = { projectId, milestoneId ->
                    // TODO add navigation to add and edit milestone screen
                    navController.navigate(
                        "${Screens.AddAndEditMilestoneScreen.route}/$projectId/$milestoneId"
                        //  "${Screens.AddAndEditScreens.route}/${-1}"
                    )
                },
                onClickIconBack = {
                    navController.popBackStack()
                },
                navigateToAllProjects = {
                    navController.navigateUp() // navigate(Screens.AllProjectsScreens.route)
                }
            )
            // TODO - add a milestones screen
        }
        composable(route = Screens.MilestonesScreens.route) {
            isBottomBarVisible(true)
            MilestonesScreens()
            // TODO - add a milestones screen
        }
        composable(route = Screens.Notifications.route) {
            isBottomBarVisible(true)
            NotificationsScreens()
            // TODO - add a milestones screen
        }
    }
}

@Composable
fun MilestonesScreens() {
    Text(modifier = Modifier.testTag("heading"), text = "Milestones Screen")
}

@Composable
fun NotificationsScreens() {
    Text(modifier = Modifier.testTag("heading"), text = "Notifications Screen")
}
