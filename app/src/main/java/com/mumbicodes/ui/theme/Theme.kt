package com.mumbicodes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BlueSecondary,
    secondary = BlueAccent,
   /* surface = TextColorDark,
    background = darkBg,
    onPrimary = BluePrimary,
    onBackground = BlueAccent,
    onSurface = BlueAccent*/
)

private val LightColorPalette = lightColors(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = BlueAccent,
    onPrimary = White,
    onSecondary = Black,
    onBackground = TextColorDark,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ProjectTrackingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}