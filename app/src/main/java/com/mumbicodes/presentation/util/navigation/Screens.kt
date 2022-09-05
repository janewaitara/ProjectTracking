package com.mumbicodes.presentation.util.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mumbicodes.R
import com.mumbicodes.presentation.util.PROJECT_ID

sealed class Screens(
    val route: String,
    @DrawableRes val outlinedIcon: Int,
    @DrawableRes val filledIcon: Int,
    val title: String,
) {
    object AllProjectsScreens :
        Screens(
            "allProjects",
            R.drawable.ic_home_outlined,
            R.drawable.ic_home_filled,
            "Home"
        )

    object AddAndEditScreens :
        Screens(
            "addAndEdit",
            R.drawable.ic_add_outlined,
            R.drawable.ic_add_filled,
            "Add"
        ) {
        const val projectId = PROJECT_ID
        val routeWithArgs = "$route/{$projectId}"
        val arguments = listOf(
            navArgument(projectId) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    }

    object MilestonesScreens : Screens(
        "milestones",
        R.drawable.ic_tasks_outlined,
        R.drawable.ic_tasks_filled,
        "Milestones"
    )

    object ProjectDetails :
        Screens(
            "projectDetails",
            R.drawable.ic_add_outlined,
            R.drawable.ic_add_filled,
            "Details"
        )

    object Notifications :
        Screens(
            "notifications",
            R.drawable.ic_notifications_outlined,
            R.drawable.ic_notifications_filled,
            "Notifications"
        )
}

val bottomNavigationDestinations = listOf(
    Screens.AllProjectsScreens,
    Screens.AddAndEditScreens,
    Screens.MilestonesScreens,
    Screens.Notifications,
)
