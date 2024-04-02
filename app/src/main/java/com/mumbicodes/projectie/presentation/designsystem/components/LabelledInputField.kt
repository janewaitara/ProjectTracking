package com.mumbicodes.projectie.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.designsystem.theme.GreySubtle
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space48dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space4dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space8dp
import com.mumbicodes.projectie.presentation.designsystem.theme.White
import com.mumbicodes.projectie.presentation.screens.add_edit_milestone.TaskState
import com.mumbicodes.projectie.presentation.screens.add_edit_milestone.TaskTextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledInputField(
    modifier: Modifier = Modifier,
    fieldLabel: String = "label",
    placeholder: String = " Placeholder",
    textValue: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    maxLine: Int = 1,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = fieldLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inverseSurface
        )
        Spacer(modifier = Modifier.height(Space8dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(0.dp)
                .heightIn(min = Space48dp)
                .fillMaxWidth(),
            value = textValue,
            textStyle = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    modifier = Modifier,
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = GreySubtle
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
            ),
            maxLines = maxLine,
            singleLine = singleLine,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledInputFieldWithIcon(
    modifier: Modifier = Modifier,
    fieldLabel: String = "label",
    placeholder: String = " Placeholder",
    textValue: String = "",
    @DrawableRes vectorIconId: Int,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = fieldLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inverseSurface
        )
        Spacer(modifier = Modifier.height(Space8dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(0.dp)
                .heightIn(min = Space48dp)
                .fillMaxWidth(),
            value = textValue,
            enabled = false,
            textStyle = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp, 24.dp),
                    painter = painterResource(id = vectorIconId),
                    contentDescription = null
                )
            },
            placeholder = {
                Text(
                    modifier = Modifier,
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = GreySubtle
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            ),
            maxLines = 1,
            singleLine = singleLine,
        )
    }
}

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
                .padding(vertical = Space4dp),
        )
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                color = GreySubtle
            )
        }
    }
}

@Composable
fun TaskItemField(
    modifier: Modifier,
    task: TaskState,
    onCheckedChange: () -> Unit,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescChange: (String) -> Unit,
    onTaskTitleFocusChange: (FocusState) -> Unit,
    onTaskDescFocusChange: (FocusState) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = provideShadowColor(),
                spotColor = provideShadowColor()
            )
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .padding(Space8dp)

    ) {
        // Create references for the composables to constrain
        val (icon, taskTitle, taskDesc) = createRefs()

        Icon(
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(taskTitle.top)
                    bottom.linkTo(taskTitle.bottom)
                }
                .clickable {
                    onCheckedChange()
                },
            painter = painterResource(id = if (task.statusState) R.drawable.ic_checkbox_true else R.drawable.ic_checkbox_false),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "Checkbox"
        )
        TransparentHintTextField(
            modifier = Modifier
                .constrainAs(taskTitle) {
                    start.linkTo(icon.end, margin = Space8dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
            text = task.taskTitleState.text,
            hint = task.taskTitleState.hint,
            onValueChange = {
                onTaskTitleChange(it)
            },
            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
            onFocusChange = {
                onTaskTitleFocusChange(it)
            },
            isHintVisible = task.taskTitleState.isHintVisible
        )

        Spacer(modifier = Modifier.height(Space4dp))

        TransparentHintTextField(
            modifier = Modifier
                .constrainAs(taskDesc) {
                    start.linkTo(taskTitle.start)
                    top.linkTo(taskTitle.bottom, margin = Space4dp)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
            text = task.taskDescState.text,
            hint = task.taskDescState.hint,
            onValueChange = {
                onTaskDescChange(it)
            },
            textStyle = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.inverseSurface),
            onFocusChange = {
                onTaskDescFocusChange(it)
            },
            isHintVisible = task.taskDescState.isHintVisible
        )
    }
}

@Preview
@Composable
fun LabelledTextFieldPreview() {
    ProjectTrackingTheme {
        LabelledInputField(
            modifier = Modifier.background(
                color = White
            ),
            onValueChange = {}
        )
    }
}

@Preview
@Composable
fun LabelledTextFieldWithIconPreview() {
    ProjectTrackingTheme {

        LabelledInputFieldWithIcon(
            modifier = Modifier.background(
                color = White
            ),
            onValueChange = {},
            vectorIconId = R.drawable.ic_calendar,
        )
    }
}

@Preview
@Composable
fun TransparentTextFieldPreview() {
    ProjectTrackingTheme {

        TransparentHintTextField(
            text = "",
            hint = "placeholder ",
            onFocusChange = {},
            onValueChange = {}
        )
    }
}

@Preview
@Composable
fun TaskItemFieldPreview() {
    ProjectTrackingTheme {

        TaskItemField(
            modifier = Modifier.background(
                color = White
            ),
            TaskState(
                milestoneId = 234,
                taskId = 432,
                initialTaskTitleState = TaskTextFieldState(
                    text = "Task Title 1",
                    hint = "thius is a placehol"
                ),
            ),
            onCheckedChange = {},
            onTaskTitleChange = {},
            onTaskDescChange = {},
            onTaskTitleFocusChange = {},
            onTaskDescFocusChange = {}
        )
    }
}
