package com.mumbicodes.projectie.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.projectie.presentation.theme.ProjectTrackingTheme
import java.util.*

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),

    ) {
        Text(
            text = text.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    enabledContentColor: Color = MaterialTheme.colorScheme.primary,
    disabledContentColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val borderColor = when (isEnabled) {
        true -> enabledContentColor
        false -> disabledContentColor
    }
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        border = BorderStroke(2.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = enabledContentColor,
            disabledContentColor = disabledContentColor,
        ),
    ) {
        Text(
            text = text.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    ProjectTrackingTheme {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary button",
            onClick = {},
            isEnabled = true
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    ProjectTrackingTheme {
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary button",
            onClick = {},
            isEnabled = false
        )
    }
}
