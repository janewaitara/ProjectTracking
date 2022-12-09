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
import com.mumbicodes.presentation.theme.*
import com.mumbicodes.presentation.util.navigation.Screens
import com.mumbicodes.presentation.util.navigation.navigationRailDestinations

@Composable
@Preview
fun NavigationDrawerComposable(
    navController: NavController = rememberNavController(),
    onItemClick: (Screens) -> Unit = {},
    onAddClick: () -> Unit = {},
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navStackBackEntry?.destination

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Space24dp, vertical = Space32dp),
    ) {

        Spacer(modifier = Modifier.height(Space32dp))

        ExtendedFloatingActionButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.addTitle),
            )

            Spacer(modifier = Modifier.width(Space12dp))
            Text(
                text = stringResource(id = R.string.addProject),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier

            )
        }
        Spacer(modifier = Modifier.height(Space32dp))

        navigationRailDestinations.forEach { screen ->
            AddDrawerItem(
                screen = screen,
                selectedDestination = selectedDestination,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
fun AddDrawerItem(
    screen: Screens,
    selectedDestination: NavDestination?,
    onItemClick: (Screens) -> Unit,
) {
    val currentScreen = navigationRailDestinations.find {
        it.route == selectedDestination?.route
    } ?: Screens.AllProjectsScreens

    val selected = screen.route == currentScreen.route

    val background =
        if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    val contentColor =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    val dividerColor =
        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    val textTypography =
        if (selected) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                onClick = {
                    onItemClick(screen)
                }
            ),
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = Space8dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space16dp)
        ) {

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .clip(shape = MaterialTheme.shapes.small),
                color = dividerColor, thickness = 4.dp,
            )
            Icon(
                painter = painterResource(id = if (selected) screen.filledIcon else screen.outlinedIcon),
                contentDescription = screen.title,
                tint = contentColor
            )

            Text(
                text = screen.title,
                style = textTypography.copy(color = contentColor),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview
fun DrawerLightPreview() {
    ProjectTrackingTheme {
        NavigationDrawerComposable()
    }
}

@Composable
@Preview
fun DrawerDarkPreview() {
    ProjectTrackingTheme(darkTheme = true) {
        NavigationDrawerComposable()
    }
}
