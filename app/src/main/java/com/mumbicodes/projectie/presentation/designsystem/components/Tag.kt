package com.mumbicodes.projectie.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.designsystem.theme.LightSuccess
import com.mumbicodes.projectie.presentation.designsystem.theme.LightWarning
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Red90
import com.mumbicodes.projectie.presentation.designsystem.theme.RedMain
import com.mumbicodes.projectie.presentation.designsystem.theme.Space12dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space4dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Success
import com.mumbicodes.projectie.presentation.designsystem.theme.Warning
import kotlin.math.abs

@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    numberOfDaysRemaining: Int,
) {
    val bgColor: Color
    val textColor: Color

    when {
        numberOfDaysRemaining > 0 -> {
            when (numberOfDaysRemaining) {
                in 0..3 -> {
                    bgColor = Red90
                    textColor = RedMain
                }
                in 4..9 -> {
                    bgColor = LightWarning
                    textColor = Warning
                }
                else -> {
                    bgColor = LightSuccess
                    textColor = Success
                }
            }
        }
        else -> {
            bgColor = Red90
            textColor = RedMain
        }
    }

    val daysRemaining = when {
        numberOfDaysRemaining >= 0 -> {
            when (numberOfDaysRemaining) {
                0 -> stringResource(id = R.string.today, numberOfDaysRemaining)
                1 -> stringResource(id = R.string.tomorrow, numberOfDaysRemaining)
                else -> stringResource(id = R.string.daysRemaining, numberOfDaysRemaining)
            }
        }
        else -> {
            when (val daysPassed = abs(numberOfDaysRemaining)) {
                1 -> stringResource(id = R.string.yesterday, daysPassed)
                else -> stringResource(id = R.string.passedDays, daysPassed)
            }
        }
    }

    Text(
        modifier = modifier
            .background(
                color = bgColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = Space12dp, vertical = Space4dp),
        text = daysRemaining,
        style = MaterialTheme.typography.labelSmall.copy(color = textColor)
    )
}

@Preview
@Composable
fun TagPreview() {
    ProjectTrackingTheme {
        TagItem(numberOfDaysRemaining = 4)
    }
}
