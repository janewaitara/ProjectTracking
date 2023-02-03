package com.mumbicodes.projectie.presentation.allProjects.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.presentation.components.provideShadowColor
import com.mumbicodes.projectie.presentation.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.theme.Space8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectItem(
    modifier: Modifier = Modifier,
    project: Project,
    onClickProject: (Int) -> Unit = {},
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        onClick = { onClickProject(project.projectId) },
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .shadow(
                elevation = 40.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = provideShadowColor(),
                spotColor = provideShadowColor()
            ),

    ) {
        Column(modifier = Modifier.padding(Space8dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = project.projectName,
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )

            Spacer(modifier = Modifier.height(Space8dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // adds three dots after 2 lines
                text = project.projectDesc,
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.inverseSurface)
            )

            Spacer(modifier = Modifier.height(Space8dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.projectDeadline,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Preview
@Composable
fun ProjectCardPreview() {
    ProjectTrackingTheme {
        ProjectItem(
            project = Project(
                projectName = "Portfolio",
                projectDesc = "The design of my personal portfolio",
                projectDeadline = "12th Dec 2022",
                projectStatus = "Complete",
                timeStamp = 12
            ),
        )
    }
}
