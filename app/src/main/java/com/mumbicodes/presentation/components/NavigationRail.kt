package com.mumbicodes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.ProjectTrackingTheme
import com.mumbicodes.presentation.theme.Space16dp
import com.mumbicodes.presentation.theme.Space32dp
import com.mumbicodes.presentation.theme.Space36dp
import com.mumbicodes.presentation.util.navigation.Screens
import com.mumbicodes.presentation.util.navigation.bottomNavigationDestinations
import com.mumbicodes.presentation.util.navigation.navigationRailDestinations

@Composable
@Preview
fun NavigationRailComposable(
    navController: NavController = rememberNavController(),
    onItemClick: (Screens) -> Unit = {},
    onAddClick: () -> Unit = {},
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navStackBackEntry?.destination

    NavigationRail(modifier = Modifier.fillMaxHeight()) {

        Spacer(modifier = Modifier.height(Space32dp))

        FloatingActionButton(
            onClick = onAddClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = MaterialTheme.shapes.large
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.addTitle),
            )
        }
        Spacer(Modifier.height(Space16dp))

        navigationRailDestinations.forEach { screen ->
            AddRailItem(
                screen = screen,
                selectedDestination = selectedDestination,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
fun AddRailItem(
    screen: Screens,
    selectedDestination: NavDestination?,
    onItemClick: (Screens) -> Unit,
) {
    val currentScreen = bottomNavigationDestinations.find {
        it.route == selectedDestination?.route
    } ?: Screens.AllProjectsScreens

    val selected = screen.route == currentScreen.route

    val background =
        if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    val contentColor =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    val dividerColor =
        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .background(background)
            .clickable(
                onClick = {
                    onItemClick(screen)
                }
            ),
    ) {

        Divider(
            modifier = Modifier
                .height(Space36dp)
                .width(2.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .align(Alignment.CenterStart),
            color = dividerColor, thickness = 4.dp,
        )

        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = if (selected) screen.filledIcon else screen.outlinedIcon),
            contentDescription = screen.title,
            tint = contentColor
        )
    }
}

@Composable
@Preview
fun RailLightPreview() {
    ProjectTrackingTheme {
        NavigationRailComposable()
    }
}

@Composable
@Preview
fun RailDarkPreview() {
    ProjectTrackingTheme(darkTheme = true) {
        NavigationRailComposable()
    }
}
