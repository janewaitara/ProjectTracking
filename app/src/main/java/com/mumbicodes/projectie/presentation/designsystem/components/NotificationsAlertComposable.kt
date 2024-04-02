package com.mumbicodes.projectie.presentation.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space4dp

@Composable
fun NotificationsAlertComposable(modifier: Modifier = Modifier, onClick: () -> Unit) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(Space16dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notifications_active),
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(Space16dp))

        Column(modifier = Modifier) {

            Text(
                text = stringResource(id = R.string.notifications),
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Space4dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(
                                textDecoration = TextDecoration.Underline,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                    ) {
                        append("Notify")
                    }
                    withStyle(
                        style = MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                    ) {
                        append(" me when deadlines approach.")
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable { onClick() }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AlertPreview() {
    ProjectTrackingTheme() {
        NotificationsAlertComposable(modifier = Modifier, onClick = {})
    }
}
