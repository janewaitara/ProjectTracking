package com.mumbicodes.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = BlueSubtle,
    onPrimary = BlueDarker,
    primaryContainer = BlueMain,
    onPrimaryContainer = BlueLightest,
    inversePrimary = BlueMain,

    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    background = GreyDarkest,
    onBackground = GreySubtle,

    surface = GreyDark,
    onSurface = White,
    inverseSurface = GreySubtle,
    inverseOnSurface = GreyDarkest,
    surfaceVariant = GreyDark,
    onSurfaceVariant = White,

    outline = GreyNormal
)

private val LightColorPalette = lightColorScheme(

    primary = BlueMain,
    onPrimary = BlueLightest,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDark,
    inversePrimary = BlueSubtle,

    error = RedMain,
    onError = Red90,
    errorContainer = Red80,
    onErrorContainer = Red20,

    background = BlueLightest,
    onBackground = GreyDark,

    surface = White,
    onSurface = GreyDark,
    inverseSurface = GreyDarkest,
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
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
