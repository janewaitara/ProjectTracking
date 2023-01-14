package com.mumbicodes.presentation.splash

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.mumbicodes.R
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.theme.*
import com.mumbicodes.presentation.util.OnBoardingPage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.sign

@ExperimentalPagerApi
@Composable
fun OnBoardingScreen(
    onGetStartedClicked: () -> Unit = {},
) {
    val pages = listOf(
        OnBoardingPage.Screen1,
        OnBoardingPage.Screen2,
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 72.dp, horizontal = Space32dp)
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            OnBoardingPage(onBoardingPage = pages[position])
        }

        Spacer(modifier = Modifier.height(Space24dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            PageIndicator(
                modifier = Modifier.weight(1f),
                size = pages.size,
                index = pagerState.currentPage,
                pagerState = pagerState,
            )

            PrimaryButton(
                modifier = Modifier
                    .weight(1f),
                text = if (pagerState.currentPage == 1) {
                    stringResource(id = R.string.getStarted)
                } else {
                    stringResource(id = R.string.next)
                },
                onClick = {
                    when (pagerState.currentPage) {
                        pagerState.pageCount - 1 -> {
                            onGetStartedClicked()
                        }
                        else -> {
                            coroutineScope.launch {
                                pagerState.scrollToPage(page = pagerState.currentPage + 1)
                            }
                        }
                    }
                },
                isEnabled = true
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun PageIndicator(
    modifier: Modifier,
    size: Int,
    index: Int,
    pagerState: PagerState,
    pageIndexMapping: (Int) -> Int = { it },
    pageCount: Int = pagerState.pageCount,
) {
    val indicatorWidthPx = LocalDensity.current.run { Space16dp.roundToPx() }
    val spacingPx = LocalDensity.current.run { Space16dp.roundToPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space12dp)
        ) {
            repeat(size) {
                Indicator(isSelected = it == index)
            }
        }

        //The animating circle - drew inspo from the pagerIndicator
        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffset
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat()
                        )

                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(),
                        y = 0
                    )
                }
                .size(width = Space8dp, height = Space8dp)
                .then(
                    if (pageCount > 0) Modifier.background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    )
                    else Modifier
                )
        )
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) Space24dp else Space16dp,
        //  animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    val color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    Box(
        modifier = Modifier
            .height(Space8dp)
            .width(width.value)
            .clip(CircleShape)
            .background(color)
    ) {
    }
}

@Composable
fun OnBoardingPage(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(Space24dp))

        Image(
            modifier = Modifier.height(200.dp),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "onBoarding image"
        )

        Spacer(modifier = Modifier.height(Space48dp))
        Spacer(modifier = Modifier.height(Space36dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = onBoardingPage.title,
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )
        Spacer(modifier = Modifier.height(Space16dp))

        Text(
            modifier = Modifier.padding(start = Space16dp, end = Space16dp),
            text = onBoardingPage.description,
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space48dp))
    }
}

@ExperimentalPagerApi
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun OnBoarding2Preview() {
    ProjectTrackingTheme {
        OnBoardingScreen(onGetStartedClicked = {})
    }
}
