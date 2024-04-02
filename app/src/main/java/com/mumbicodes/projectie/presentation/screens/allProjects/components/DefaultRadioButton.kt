package com.mumbicodes.projectie.presentation.screens.allProjects.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.presentation.designsystem.theme.Space36dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space4dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRadioButton(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(Space36dp)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onSelect()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.outline,
            ),
        )
        Spacer(modifier = Modifier.height(Space4dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.inverseSurface),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun RadioButtonPreview() {
    DefaultRadioButton(
        "Hello",
        false,
        { }
    )
}
