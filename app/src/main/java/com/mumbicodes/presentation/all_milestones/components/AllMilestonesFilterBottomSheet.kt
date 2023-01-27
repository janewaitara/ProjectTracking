package com.mumbicodes.presentation.all_milestones.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.R
import com.mumbicodes.domain.util.AllMilestonesOrder
import com.mumbicodes.presentation.allProjects.components.DefaultRadioButton
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.components.SecondaryButton
import com.mumbicodes.presentation.theme.*

@Composable
fun FilterMilestonesBottomSheetContent(
    modifier: Modifier = Modifier,
    milestonesOrder: AllMilestonesOrder = AllMilestonesOrder.MostUrgent,
    selectedMilestonesOrder: AllMilestonesOrder,
    onOrderChange: (AllMilestonesOrder) -> Unit,
    onFiltersApplied: () -> Unit,
    onFiltersReset: () -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(Space20dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = stringResource(id = R.string.filterMilestones),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Space24dp))

        Text(
            text = stringResource(id = R.string.filterBy),
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.inverseSurface),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Space12dp))

        // Used user's selection state since the selection has not been published
        DefaultRadioButton(
            text = stringResource(id = R.string.mostUrgent),
            isSelected = selectedMilestonesOrder is AllMilestonesOrder.MostUrgent,
            onSelect = { onOrderChange(AllMilestonesOrder.MostUrgent) },
            modifier = Modifier.fillMaxWidth(),
        )

        DefaultRadioButton(
            text = stringResource(id = R.string.leastUrgent),
            isSelected = selectedMilestonesOrder is AllMilestonesOrder.LeastUrgent,
            onSelect = { onOrderChange(AllMilestonesOrder.LeastUrgent) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Space24dp))

        Spacer(Modifier.height(Space24dp))

        // need a way to compare whether the selected and milestone order have the same value
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.applyFilters),
            onClick = onFiltersApplied,
            isEnabled = milestonesOrder !is AllMilestonesOrder.MostUrgent ||
                selectedMilestonesOrder !is AllMilestonesOrder.MostUrgent
        )

        Spacer(Modifier.height(Space8dp))

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.resetFilters),
            onClick = onFiltersReset,
            isEnabled = milestonesOrder !is AllMilestonesOrder.MostUrgent,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun FilterBottomSheetContent() {
    ProjectTrackingTheme {
        FilterMilestonesBottomSheetContent(
            milestonesOrder = AllMilestonesOrder.MostUrgent,
            selectedMilestonesOrder = AllMilestonesOrder.LeastUrgent,
            onOrderChange = {},
            onFiltersApplied = { /*TODO*/ },
            onFiltersReset = { }
        )
    }
}
