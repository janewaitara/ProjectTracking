package com.mumbicodes.projectie.presentation.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mumbicodes.projectie.presentation.designsystem.theme.GreyLight
import com.mumbicodes.projectie.presentation.designsystem.theme.GreySubtle
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space12dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space24dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space48dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space8dp

@Composable
fun ShimmerEffectComposable() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        // The mask with the shimmer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .shimmerEffect()
        ) {}

        // The white items
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(
                Modifier
                    .height(Space48dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier
                        .height(Space16dp)
                        .weight(2f)
                        .clip(shape = MaterialTheme.shapes.small)
                )
                Spacer(
                    modifier = Modifier
                        .height(Space16dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                )
            }

            Spacer(
                Modifier
                    .height(Space12dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
            )
            Spacer(
                modifier = Modifier
                    .height(Space16dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .fillMaxWidth()
            )

            Spacer(
                Modifier
                    .height(Space48dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .weight(1f)
                )
                Spacer(
                    Modifier
                        .width(Space8dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                )
                Spacer(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .weight(1f)
                )
                Spacer(
                    Modifier
                        .width(Space8dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                )
                Spacer(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .weight(1f)
                )
                Spacer(
                    Modifier
                        .width(Space8dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(shape = MaterialTheme.shapes.small)
                        .weight(1f)
                )
            }

            Spacer(
                Modifier
                    .height(Space24dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
            )

            repeat(9) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(Space12dp)
                            .background(MaterialTheme.colorScheme.background)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(Space16dp)
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.small)
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(Space12dp)
                            .background(MaterialTheme.colorScheme.background)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(
                            modifier = Modifier
                                .height(Space16dp)
                                .weight(1f)
                                .clip(shape = MaterialTheme.shapes.small)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(Space16dp)
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }
                }

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(Space12dp)
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val shimmerColors = listOf(
        provideShimmerColor().copy(alpha = 0.5f),
        provideShimmerColor(),
        provideShimmerColor().copy(alpha = 0.5f),
    )
    val transition = rememberInfiniteTransition()

    val translateAnimation by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    )

    background(
        brush = Brush.horizontalGradient(
            colors = shimmerColors,
            startX = translateAnimation,
            endX = translateAnimation + size.width.toFloat()
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

@Composable
fun provideShimmerColor(): Color {
    val darkTheme: Boolean = isSystemInDarkTheme()
    return if (darkTheme) GreySubtle else GreyLight
}

@Composable
@Preview(showBackground = true)
fun ShimmerEffectComposablePreview() {
    ProjectTrackingTheme {
        ShimmerEffectComposable()
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ShimmerEffectComposableDarkPreview() {
    ProjectTrackingTheme {
        ShimmerEffectComposable()
    }
}
