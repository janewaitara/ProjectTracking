package com.mumbicodes.presentation.allProjects.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.R
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.components.SecondaryButton
import com.mumbicodes.presentation.theme.GreyNormal
import com.mumbicodes.presentation.theme.ProjectTrackingTheme

@Composable
fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    projectsOrder: ProjectsOrder = ProjectsOrder.DateAdded(OrderType.Descending),
    selectedProjectsOrder: ProjectsOrder,
    onOrderChange: (ProjectsOrder) -> Unit,
    onFiltersApplied: () -> Unit,
    onFiltersReset: () -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = stringResource(id = R.string.filter),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.filterBy),
            style = MaterialTheme.typography.titleMedium.copy(color = GreyNormal),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))

        // Used user's selection state since the selection has not been published
        DefaultRadioButton(
            text = stringResource(id = R.string.dateCreated),
            isSelected = projectsOrder is ProjectsOrder.DateAdded,
            onSelect = { onOrderChange(ProjectsOrder.DateAdded(selectedProjectsOrder.orderType)) },
            modifier = Modifier.fillMaxWidth(),
        )

        DefaultRadioButton(
            text = stringResource(id = R.string.projectName),
            isSelected = projectsOrder is ProjectsOrder.Name,
            onSelect = { onOrderChange(ProjectsOrder.Name(selectedProjectsOrder.orderType)) },
            modifier = Modifier.fillMaxWidth(),
        )

        DefaultRadioButton(
            text = stringResource(id = R.string.projectDeadline),
            isSelected = projectsOrder is ProjectsOrder.Deadline,
            onSelect = { onOrderChange(ProjectsOrder.Deadline(selectedProjectsOrder.orderType)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.orderBy),
            style = MaterialTheme.typography.titleMedium.copy(color = GreyNormal),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))

        DefaultRadioButton(
            text = stringResource(id = R.string.descending),
            isSelected = projectsOrder.orderType is OrderType.Descending,
            onSelect = { onOrderChange(selectedProjectsOrder.copy(OrderType.Descending)) },
            modifier = Modifier.fillMaxWidth(),
        )

        DefaultRadioButton(
            text = stringResource(id = R.string.ascending),
            isSelected = projectsOrder.orderType is OrderType.Ascending,
            onSelect = { onOrderChange(selectedProjectsOrder.copy(OrderType.Ascending)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = stringResource(id = R.string.applyFilters),
            onClick = onFiltersApplied,
            isEnabled = projectsOrder !is ProjectsOrder.DateAdded || projectsOrder.orderType !is OrderType.Descending
        )

        Spacer(Modifier.height(8.dp))

        SecondaryButton(
            text = stringResource(id = R.string.resetFilters),
            onClick = onFiltersReset,
            isEnabled = projectsOrder !is ProjectsOrder.DateAdded || projectsOrder.orderType !is OrderType.Descending
        )
    }
}

@Preview
@Composable
fun FilterBottomSheetContent() {
    ProjectTrackingTheme {
        FilterBottomSheetContent(
            onOrderChange = {},
            onFiltersApplied = {},
            onFiltersReset = {},
            projectsOrder = ProjectsOrder.Name(OrderType.Ascending),
            selectedProjectsOrder = ProjectsOrder.Name(OrderType.Ascending),
        )
    }
}
