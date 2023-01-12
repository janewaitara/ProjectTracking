package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mumbicodes.presentation.components.NavigationDrawerComposable
import com.mumbicodes.presentation.util.ContentType
import com.mumbicodes.presentation.util.NavigationType
import com.mumbicodes.presentation.util.ProjectAppContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapperUi(
    navigationType: NavigationType,
    contentType: ContentType,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ProjectsNavigationActions(navController)
    }

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                NavigationDrawerComposable(
                    navController = navController,
                    onItemClick = { screen ->
                        navigationActions.navigateTo(screen.route)
                    },
                    onAddClick = {
                        ProjectsNavigationActions(navController = navController)
                            .navigateTo("${Screens.AddAndEditScreens.route}/${-1}")
                    }
                )
            }
        ) {
            ProjectAppContent(
                navigationType = navigationType,
                contentType = contentType,
                navController = navController,
                navigateToDestination = navigationActions::navigateTo,
                onDrawerClicked = {},
                windowWidthSizeClass = windowWidthSizeClass,
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                NavigationDrawerComposable(
                    navController = navController,
                    onItemClick = { screen ->
                        navigationActions.navigateTo(screen.route)
                    },
                    onAddClick = {
                        ProjectsNavigationActions(navController = navController)
                            .navigateTo("${Screens.AddAndEditScreens.route}/${-1}")
                    }
                )
            },
            drawerState = drawerState
        ) {
            ProjectAppContent(
                navigationType = navigationType,
                contentType = contentType,
                navController = navController,
                navigateToDestination = navigationActions::navigateTo,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                windowWidthSizeClass = windowWidthSizeClass,
            )
        }
    }
}

class ProjectsNavigationActions(private val navController: NavHostController) {
    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
