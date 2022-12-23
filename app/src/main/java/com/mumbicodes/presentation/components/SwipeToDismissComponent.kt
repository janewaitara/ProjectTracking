package com.mumbicodes.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissComponent(
    onSwipeAction: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                onSwipeAction()
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {

            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 1.0f else 1.2f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.onError,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(end = Space20dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.scale(scale),
                        painter = painterResource(id = R.drawable.ic_delete),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = stringResource(id = R.string.delete),
                    )
                    Spacer(modifier = Modifier.height(Space4dp))
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                    )
                }
            }
        },
        dismissContent = {
            content()
        }
    )
}
