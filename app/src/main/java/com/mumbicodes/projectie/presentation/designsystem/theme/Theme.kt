package com.mumbicodes.projectie.presentation.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = BlueSubtle,
    onPrimary = BlueDarker,
    primaryContainer = BlueDark,
    onPrimaryContainer = BlueLightest,
    inversePrimary = BlueMain,

    secondary = SubtleWarning,
    onSecondary = DarkestWarning,
    secondaryContainer = DarkerWarning,
    onSecondaryContainer = LightWarning,

    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    background = GreyDarkest,
    onBackground = GreySubtle,

    surface = GreyDark,
    onSurface = White,
    inverseSurface = GreyLight, // TODO Change this for contrast
    inverseOnSurface = GreySubtle,
    surfaceVariant = GreyNormal,
    onSurfaceVariant = White,

    outline = GreySubtle
)

private val LightColorPalette = lightColorScheme(

    primary = BlueMain,
    onPrimary = BlueLightest,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDark,
    inversePrimary = BlueSubtle,

    secondary = Warning,
    onSecondary = LightWarning,
    secondaryContainer = SubtleWarning,
    onSecondaryContainer = DarkWarning,

    error = RedMain,
    onError = Red90,
    errorContainer = Red80,
    onErrorContainer = Red20,

    background = BlueLightest,
    onBackground = GreyDark,

    surface = White,
    onSurface = GreyDark,
    inverseSurface = GreyNormal,
    inverseOnSurface = GreySubtle,
    surfaceVariant = White,
    onSurfaceVariant = GreyDark,

    outline = GreySubtle
)

@Composable
fun ProjectTrackingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // when the version 12 check is out of the when statement, lint fails on github actions
    val colors = when {
        /*Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }*/
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insets = WindowCompat.getInsetsController(window, view)
            window.statusBarColor = colors.background.toArgb() // choose a status bar color
            insets.isAppearanceLightStatusBars = !darkTheme
            window.navigationBarColor = colors.background.toArgb() // choose a navigation bar color
            insets.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
