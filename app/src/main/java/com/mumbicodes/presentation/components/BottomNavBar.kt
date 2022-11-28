package com.mumbicodes.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mumbicodes.presentation.theme.Space20dp
import com.mumbicodes.presentation.util.navigation.Screens
import com.mumbicodes.presentation.util.navigation.bottomNavigationDestinations

@Composable
fun BottomBar(
    navController: NavHostController,
    onItemClick: (Screens) -> Unit,
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .height(72.dp)
            .padding(top = 8.dp, bottom = 8.dp)
            .background(Color.White)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomNavigationDestinations.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
fun AddItem(
    screen: Screens,
    currentDestination: NavDestination?,
    onItemClick: (Screens) -> Unit,
) {

    val currentScreen = bottomNavigationDestinations.find {
        it.route == currentDestination?.route
    } ?: Screens.AllProjectsScreens

    val selected = screen.route == currentScreen.route

    val isAdd = screen.route == "addAndEdit"

    val background =
        if (isAdd) MaterialTheme.colorScheme.primary else Color.Transparent

    val contentColor =
        if (isAdd) {
            MaterialTheme.colorScheme.primary
        } else {
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        }

    Box(
        modifier = Modifier
            // .height(40.dp)
            // .clip(CircleShape)
            // .background(background)
            .clickable(
                onClick = {
                    onItemClick(screen)
                }
            )
    ) {

        Column(
            modifier = Modifier,
            // .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedVisibility(visible = selected) {
                Divider(modifier = Modifier.width(Space20dp).clip(shape = MaterialTheme.shapes.small), color = contentColor, thickness = 2.dp,)
            }

            Icon(
                painter = painterResource(id = if (selected) screen.filledIcon else screen.outlinedIcon),
                contentDescription = screen.title,
                tint = contentColor
            )
        }
    }
}

@Composable
@Preview
fun BottomNav2Preview() {
    BottomBar(navController = rememberNavController(), onItemClick = {})
}
