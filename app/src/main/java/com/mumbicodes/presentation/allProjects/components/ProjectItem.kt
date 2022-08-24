package com.mumbicodes.presentation.allProjects.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.domain.model.Project
import com.mumbicodes.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectItem(
    modifier: Modifier = Modifier,
    project: Project,
    onClickProject: (Int) -> Unit = {},
) {
    Card(
        // the Material color is not working - MaterialTheme.colorScheme.surface
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 40.dp),
        shape = MaterialTheme.shapes.small,
        onClick = { onClickProject(project.projectId) },
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 40.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = Color(0xFFCCCCCC).copy(alpha = 0.9f),
                spotColor = Color(0xFFCCCCCC).copy(alpha = 0.9f)
            ),

    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.projectName,
                style = MaterialTheme.typography.headlineMedium.copy(color = GreyDark)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // adds three dots after 2 lines
                text = project.projectDesc,
                style = MaterialTheme.typography.labelMedium.copy(color = GreyNormal)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.projectDeadline,
                style = MaterialTheme.typography.labelSmall.copy(color = BlueMain)
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
