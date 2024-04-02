package com.mumbicodes.projectie.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.util.navigation.Screens
import com.mumbicodes.projectie.presentation.util.navigation.bottomNavigationDestinations

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (Screens) -> Unit,
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    // Fetch your currentDestination:
    val currentDestination = currentBackStack?.destination

    // Change the variable to this and use home as a backup screen if this returns null
    val currentScreen = bottomNavigationDestinations.find {
        it.route == currentDestination?.route
    } ?: Screens.AllProjectsScreens

    BottomAppBar(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        bottomNavigationDestinations.forEach { destination ->
            val selected = destination.route == currentScreen.route
            NavigationBarItem(
                selected = selected,
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) destination.filledIcon else destination.outlinedIcon),
                        contentDescription = destination.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.outline
                ),
                onClick = { onItemClick(destination) },
            )
        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    ProjectTrackingTheme {
        BottomNavigationBar(
            navController = rememberNavController(),
            onItemClick = {}
        )
    }
}
