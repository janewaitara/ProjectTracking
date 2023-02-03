package com.mumbicodes.projectie.presentation.util.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.mumbicodes.projectie.presentation.util.ContentType
import com.mumbicodes.projectie.presentation.util.DevicePosture
import com.mumbicodes.projectie.presentation.util.NavigationType

@Composable
fun ProjectAppComposable(
    windowWidthSizeClass: WindowWidthSizeClass,
    foldingPosture: DevicePosture,
) {
    val navigationType: NavigationType
    val contentType: ContentType

    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = if (foldingPosture != DevicePosture.NormalPosture) {
                ContentType.DUAL_PANE
            } else {
                ContentType.SINGLE_PANE
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingPosture is DevicePosture.BookPosture) {
                NavigationType.NAVIGATION_RAIL
            } else {
                NavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = ContentType.DUAL_PANE
        }
        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
    }

    NavigationWrapperUi(
        navigationType = navigationType,
        contentType = contentType,
        windowWidthSizeClass = windowWidthSizeClass,
    )
}
