package com.mumbicodes.projectie.presentation.projectDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.presentation.components.PrimaryButton
import com.mumbicodes.projectie.presentation.components.SecondaryButton
import com.mumbicodes.projectie.presentation.projectDetails.components.TaskItem
import com.mumbicodes.projectie.presentation.theme.*
import com.mumbicodes.projectie.presentation.util.toDateAsString

@Composable
fun MilestoneDetailsBottomSheetContent(
    modifier: Modifier = Modifier,
    milestoneWithTasks: MilestoneWithTasks,
    onDeleteClicked: (Milestone) -> Unit,
    onModifyClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(Space20dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = milestoneWithTasks.milestone.milestoneTitle,
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Space24dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.schedule),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    0.3f
                )
            ),
        )
        Spacer(Modifier.height(Space8dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.bodySmall.toSpanStyle()
                        .copy(MaterialTheme.colorScheme.inverseSurface)
                ) {
                    append(milestoneWithTasks.milestone.milestoneSrtDate.toDateAsString("dd MMM yyyy"))
                }
                withStyle(
                    style = MaterialTheme.typography.bodySmall.toSpanStyle()
                        .copy(MaterialTheme.colorScheme.inverseSurface)
                ) {
                    append(" to ")
                }
                withStyle(
                    style = MaterialTheme.typography.bodySmall.toSpanStyle()
                        .copy(MaterialTheme.colorScheme.inverseSurface)
                ) {
                    append(milestoneWithTasks.milestone.milestoneEndDate.toDateAsString("dd MMM yyyy"))
                }
            }
        )

        Spacer(Modifier.height(Space24dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.miniTasks),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    0.3f
                )
            ),
        )
        Spacer(Modifier.height(Space8dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Space16dp)
        ) {
            milestoneWithTasks.tasks.forEach { task ->
                TaskItem(modifier = Modifier, task = task, descIsVisible = true)
            }
        }

        Spacer(Modifier.height(Space48dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space12dp)
        ) {
            SecondaryButton(
                modifier = Modifier,
                text = stringResource(id = R.string.delete),
                onClick = { onDeleteClicked(milestoneWithTasks.milestone) },
                isEnabled = true
            )

            PrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.modifyMilestone),
                onClick = { onModifyClicked(milestoneWithTasks.milestone.milestoneId) },
                isEnabled = true
            )
        }
    }
}

@Preview
@Composable
fun MilestoneBottomSheetContent() {
    ProjectTrackingTheme {
        MilestoneDetailsBottomSheetContent(
            modifier = Modifier,
            milestoneWithTasks = MilestoneWithTasks(
                Milestone(
                    projectId = 1,
                    milestoneId = 2,
                    milestoneTitle = "This is a milestone title",
                    milestoneSrtDate = 19236,
                    milestoneEndDate = 19247,
                    status = "Not started",
                ),
                tasks = listOf(
                    Task(
                        taskId = 1,
                        milestoneId = 2,
                        taskTitle = "Display Projects",
                        taskDesc = "Display Projects",
                        status = true
                    ),
                    Task(
                        taskId = 2,
                        milestoneId = 2,
                        taskTitle = "Bottom navigation",
                        taskDesc = "Bottom navigation",
                        status = false
                    ),
                    Task(
                        taskId = 3,
                        milestoneId = 2,
                        taskTitle = "Display Projects",
                        taskDesc = "Display Projects",
                        status = true
                    ),
                )
            ),
            onDeleteClicked = {},
            onModifyClicked = {}
        )
    }
}
