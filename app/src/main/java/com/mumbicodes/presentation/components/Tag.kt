package com.mumbicodes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.*

@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    numberOfDaysRemaining: Int,
) {
    val bgColor: Color
    val textColor: Color

    // TODO think of negative cases - these are days that have passed
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

    val daysRemaining = if (numberOfDaysRemaining == 1) {
        stringResource(id = R.string.dayRemaining, numberOfDaysRemaining)
    } else {
        stringResource(id = R.string.daysRemaining, numberOfDaysRemaining)
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
