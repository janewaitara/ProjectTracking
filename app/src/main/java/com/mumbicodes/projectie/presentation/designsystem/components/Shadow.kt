package com.mumbicodes.projectie.presentation.designsystem.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor

@Composable
fun provideShadowColor(): Color {
    val darkTheme: Boolean = isSystemInDarkTheme()
    return if (darkTheme) DefaultShadowColor else Color(0xFFCCCCCC).copy(alpha = 0.9f)
}
