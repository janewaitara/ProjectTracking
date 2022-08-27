package com.mumbicodes.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.presentation.theme.ProjectTrackingTheme
import java.util.Locale

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
) {
    val borderColor = when (isEnabled) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.primaryContainer
    }
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        border = BorderStroke(2.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer,
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
            text = "Primary button",
            onClick = {},
            isEnabled = false
        )
    }
}
