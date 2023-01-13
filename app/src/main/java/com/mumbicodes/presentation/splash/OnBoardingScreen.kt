package com.mumbicodes.presentation.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.mumbicodes.R
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.theme.*
import com.mumbicodes.presentation.util.OnBoardingPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
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
    val currentPage = pagerState.currentPage

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            count = 2,
            state = PagerState(),
            verticalAlignment = Alignment.Top
        ) { position ->
            OnBoardingPage(onBoardingPage = pages[position])
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            HorizontalPagerIndicator(
                pagerState = pagerState
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = when (currentPage) {
                    pagerState.pageCount - 1 -> stringResource(id = R.string.getStarted)
                    else -> stringResource(id = R.string.next)
                },
                onClick = {
                    when (currentPage) {
                        pagerState.pageCount - 1 -> {
                            // Navigate
                        }
                        else -> {
                            coroutineScope.launch {
                                pagerState.scrollToPage(page = currentPage + 1)
                            }
                        }
                    }
                },
                isEnabled = true
            )
        }
    }
}

@Composable
fun OnBoardingPage(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = Space32dp),
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun OnBoardingPreview() {
    ProjectTrackingTheme {
        OnBoardingPage(onBoardingPage = OnBoardingPage.Screen1)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun OnBoarding1Preview() {
    ProjectTrackingTheme {
        OnBoardingPage(onBoardingPage = OnBoardingPage.Screen2)
    }
}
