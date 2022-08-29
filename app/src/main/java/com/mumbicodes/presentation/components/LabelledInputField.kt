package com.mumbicodes.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.*

@Composable
fun LabelledInputField(
    modifier: Modifier = Modifier,
    fieldLabel: String = "label",
    placeholder: String = " Placeholder",
    textValue: String = "",
    @DrawableRes vectorImageId: Int? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = fieldLabel,
            style = MaterialTheme.typography.bodySmall,
            color = GreyNormal
        )
        Spacer(modifier = Modifier.height(Space8dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(0.dp)
                .heightIn(min = Space48dp),
            value = textValue,
            onValueChange = onValueChange,
            leadingIcon = {
                if (vectorImageId != null) {
                    Icon(
                        modifier = Modifier.size(24.dp, 24.dp),
                        painter = painterResource(id = vectorImageId),
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text(
                    modifier = Modifier,
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = GreySubtle
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            ),
            maxLines = 1,
            singleLine = true,
        )
    }
}

@Preview
@Composable
fun LabelledTextFieldPreview() {
    ProjectTrackingTheme {
        LabelledInputField(
            modifier = Modifier.background(
                color = White
            ),
            vectorImageId = R.drawable.ic_calendar,
            onValueChange = {}
        )
    }
}
