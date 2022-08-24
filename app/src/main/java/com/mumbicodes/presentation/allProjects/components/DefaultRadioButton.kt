package com.mumbicodes.presentation.allProjects.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.presentation.theme.GreyNormal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRadioButton(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(color = GreyNormal),
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
