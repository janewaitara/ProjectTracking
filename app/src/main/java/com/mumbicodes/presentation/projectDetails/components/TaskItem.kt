package com.mumbicodes.presentation.projectDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.GreyNormal
import com.mumbicodes.presentation.theme.ProjectTrackingTheme
import com.mumbicodes.presentation.theme.Space8dp

@Composable
fun TaskItem(
    modifier: Modifier,
    taskTitle: String,
    checked: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Space8dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = if (checked) R.drawable.ic_checkbox_true else R.drawable.ic_checkbox_false),
            contentDescription = "Checkbox"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = taskTitle,
            style = MaterialTheme.typography.bodySmall,
            color = GreyNormal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    ProjectTrackingTheme {
        TaskItem(modifier = Modifier.fillMaxWidth(), taskTitle = "This is a task", checked = true)
    }
}
