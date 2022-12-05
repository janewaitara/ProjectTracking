package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.mumbicodes.presentation.util.NavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapperUi(
    navigationType: NavigationType,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                // TODO add a navigation drawer
            }
        ) {
            // TODO add Content
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                // TODO add a navigation drawer content
            },
            drawerState = drawerState
        ) {
            // TODO add Content
        }
    }
}
