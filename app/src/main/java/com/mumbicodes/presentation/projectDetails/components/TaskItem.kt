package com.mumbicodes.presentation.projectDetails.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mumbicodes.R
import com.mumbicodes.domain.model.Task
import com.mumbicodes.presentation.theme.GreyNormal
import com.mumbicodes.presentation.theme.ProjectTrackingTheme
import com.mumbicodes.presentation.theme.Space4dp
import com.mumbicodes.presentation.theme.Space8dp

@Composable
fun TaskItem(
    modifier: Modifier,
    task: Task,
    descIsVisible: Boolean,
) {

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        // Create references for the composables to constrain
        val (icon, taskTitle, taskDesc) = createRefs()
        val taskTitleStyle =
            if (descIsVisible) MaterialTheme.typography.titleMedium else
                MaterialTheme.typography.bodySmall

        Icon(
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(taskTitle.top)
                bottom.linkTo(taskTitle.bottom)
            },
            painter = painterResource(id = if (task.status) R.drawable.ic_checkbox_true else R.drawable.ic_checkbox_false),
            contentDescription = "Checkbox"
        )
        Text(
            modifier = Modifier
                .constrainAs(taskTitle) {
                    start.linkTo(icon.end, margin = Space8dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
            text = task.taskTitle,
            style = taskTitleStyle,
            color = GreyNormal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (descIsVisible) {
            Text(
                modifier = Modifier
                    .constrainAs(taskDesc) {
                        start.linkTo(taskTitle.start)
                        top.linkTo(taskTitle.bottom, margin = Space4dp)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    },
                text = task.taskDesc,
                style = MaterialTheme.typography.bodySmall,
                color = GreyNormal,
            )
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    ProjectTrackingTheme {
        TaskItem(
            modifier = Modifier.fillMaxWidth(),
            task = Task(
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            descIsVisible = true
        )
    }
}
