package com.mumbicodes.projectie.presentation.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space12dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: (String) -> Unit,
) {

    Surface(
        modifier = modifier,
        color = when {
            selected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.primaryContainer
        },
        shape = MaterialTheme.shapes.small,
        onClick = { onClick(text) },
        contentColor = when {
            selected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onBackground
        }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = Space16dp, vertical = Space12dp)
        )
    }
}

@Preview
@Composable
fun ChipPreview() {
    ProjectTrackingTheme {
        FilterChip(
            text = "Test",
            selected = false,
            onClick = {}
        )
    }
}
