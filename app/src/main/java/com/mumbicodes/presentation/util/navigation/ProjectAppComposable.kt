package com.mumbicodes.presentation.util.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.mumbicodes.presentation.util.DevicePosture
import com.mumbicodes.presentation.util.NavigationType

@Composable
fun ProjectAppComposable(
    windowWidthSizeClass: WindowWidthSizeClass,
    foldingPosture: DevicePosture,
) {
    val navigationType: NavigationType
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingPosture is DevicePosture.BookPosture) {
                NavigationType.NAVIGATION_RAIL
            } else {
                NavigationType.PERMANENT_NAVIGATION_DRAWER
            }
        }
        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }
    }

    NavigationWrapperUi(navigationType = navigationType)
}
