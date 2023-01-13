package com.mumbicodes.presentation.util

import androidx.annotation.DrawableRes
import com.mumbicodes.R

sealed class OnBoardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String,
) {
    object Screen1 : OnBoardingPage(
        image = R.drawable.onboarding_illustration,
        title = "Track your Projects",
        description = "Track your projects progress and add milestones",
    )

    object Screen2 : OnBoardingPage(
        image = R.drawable.onboarding_illustration,
        title = "Receive Notifications",
        description = "Get notified when your project deadline is approaching",
    )
}
