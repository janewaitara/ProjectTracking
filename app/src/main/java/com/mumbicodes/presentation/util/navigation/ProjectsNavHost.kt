package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mumbicodes.presentation.add_edit_project.AddAndEditScreen
import com.mumbicodes.presentation.allProjects.AllProjectsScreen

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
                    "${Screens.AddAndEditScreens.route}/$projectId"
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
        composable(route = Screens.ProjectDetails.route) {
            isBottomBarVisible(false)
            ProjectDetailsScreen()
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

@Composable
fun ProjectDetailsScreen() {
    Text(modifier = Modifier.testTag("heading"), text = "Project details Screen")
}
