package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mumbicodes.presentation.ProjectsScreen
import com.mumbicodes.presentation.add_edit_milestone.AddAndEditMilestoneScreen
import com.mumbicodes.presentation.add_edit_project.AddAndEditScreen
import com.mumbicodes.presentation.all_milestones.AllMilestonesScreen
import com.mumbicodes.presentation.notifications.NotificationScreen
import com.mumbicodes.presentation.projectDetails.ProjectDetailsScreen
import com.mumbicodes.presentation.splash.OnBoardingScreen
import com.mumbicodes.presentation.splash.OnBoardingViewModel
import com.mumbicodes.presentation.util.ContentType

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectNavHost(
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentType: ContentType,
    isBottomBarVisible: (Boolean) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = onBoardingViewModel.startDestination.value,
    ) {

        composable(route = Screens.OnBoardingScreen.route) {
            isBottomBarVisible(false)
            OnBoardingScreen(
                onGetStartedClicked = {
                    navController.popBackStack()
                    navController.navigate(route = Screens.AllProjectsScreens.route)
                }
            )
        }
        composable(route = Screens.AllProjectsScreens.route) {
            isBottomBarVisible(true)
            ProjectsScreen(
                contentType = contentType,
                onClickProject = { projectId ->
                    navController.navigate(
                        "${Screens.ProjectDetails.route}/$projectId"
                    )
                },
                navController = navController,
                windowWidthSizeClass = windowWidthSizeClass
            )
           /* AllProjectsScreen(
                onClickProject = { projectId ->
                    navController.navigate(
                        *//*"${Screens.AddAndEditScreens.route}/$projectId"*//*
                        "${Screens.ProjectDetails.route}/$projectId"
                    )
                })*/
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
            AllMilestonesScreen()
            // TODO - add a milestones screen
        }
        composable(route = Screens.Notifications.route) {
            isBottomBarVisible(true)
            NotificationScreen()
            // TODO - add a milestones screen
        }
    }
}
