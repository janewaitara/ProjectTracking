package com.mumbicodes.projectie.presentation.designsystem.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space24dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space32dp
import com.mumbicodes.projectie.presentation.util.ReferenceDevices

@Composable
fun ErrorStateSlot(
    modifier: Modifier = Modifier,
    @DrawableRes illustration: Int = R.drawable.under_construction_illustration,
    @StringRes description: Int = R.string.coming_soon,
    searchParam: String,
    filter: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(Space24dp))

        Image(
            modifier = Modifier.height(200.dp),
            painter = painterResource(id = illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space16dp))
        Text(
            modifier = Modifier.padding(start = Space32dp, end = Space32dp),
            text = stringResource(id = description, searchParam, filter),
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@ReferenceDevices
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ErrorStatePreview() {
    ProjectTrackingTheme {
        EmptyStateSlot(
            illustration = R.drawable.add_project
        )
    }
}
