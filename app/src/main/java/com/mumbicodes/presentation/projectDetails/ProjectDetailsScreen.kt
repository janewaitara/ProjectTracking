package com.mumbicodes.presentation.projectDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.R
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.model.Project
import com.mumbicodes.presentation.allProjects.filters
import com.mumbicodes.presentation.components.FilterChip
import com.mumbicodes.presentation.components.PrimaryButton
import com.mumbicodes.presentation.components.SecondaryButton
import com.mumbicodes.presentation.projectDetails.components.MilestoneItem
import com.mumbicodes.presentation.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectDetailsScreen(
    projectDetailsViewModel: ProjectDetailsViewModel = hiltViewModel(),
    onEditProject: (Int) -> Unit,
    onAddOrModifyMilestone: (Int, Int) -> Unit,
    onClickIconBack: () -> Unit,
) {
    val state = projectDetailsViewModel.state.value
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    // TODO add logic to restore milestone

    ModalBottomSheetLayout(
        sheetContent = {
            MilestoneDetailsBottomSheetContent(
                milestone = state.mileStone,
                onDeleteClicked = {
                    projectDetailsViewModel.onEvent(ProjectDetailsEvents.DeleteMilestone(it))
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onModifyClicked = {
                    onAddOrModifyMilestone(state.project.projectId, it)
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            )
        },
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = GreyDark.copy(alpha = 0.5f)
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ProjectDetailsScreenContent(
                    modifier = Modifier.padding(
                        top = 24.dp,
                        start = Space20dp,
                        end = Space20dp,
                    ),
                    projectState = state,
                    onClickMilestone = { milestoneId ->
                        // TODO - it might be best to pass the milestone than the Id
                        // cause we already have it - no need to get from the db again - research
                        projectDetailsViewModel.onEvent(
                            ProjectDetailsEvents.GetMilestone(milestoneId)
                        )
                    },
                    onClickFilterMilestoneStatus = { selectedMilestoneStatus ->
                        projectDetailsViewModel.onEvent(
                            ProjectDetailsEvents.SelectedMilestonesStatus(selectedMilestoneStatus)
                        )
                    },
                    onClickIconBack = onClickIconBack,
                    onClickIconMenu = {
                        /*TODO show the menu options dropdown*/
                        projectDetailsViewModel.onEvent(
                            ProjectDetailsEvents.ToggleMenuOptionsVisibility
                        )
                    },
                    onAddMilestoneClicked = {
                        // Used that int because the function navigates to shared add/edit milestone
                        onAddOrModifyMilestone(state.project.projectId, -1)
                    },
                    onEditProject = { projectId ->
                        onEditProject(projectId)
                    },
                    onDeleteProjectClicked = {
                        projectDetailsViewModel.onEvent(ProjectDetailsEvents.ToggleDeleteDialogVisibility)
                    },
                    onDeleteProject = { project ->
                        projectDetailsViewModel.onEvent(ProjectDetailsEvents.DeleteProject(project))
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectDetailsScreenContent(
    modifier: Modifier = Modifier,
    projectState: ProjectDetailsStates,
    onClickMilestone: (Int) -> Unit,
    onClickFilterMilestoneStatus: (String) -> Unit,
    onClickIconBack: () -> Unit,
    onClickIconMenu: () -> Unit,
    onAddMilestoneClicked: () -> Unit,
    onEditProject: (Int) -> Unit,
    onDeleteProjectClicked: () -> Unit,
    onDeleteProject: (Project) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(modifier = modifier) {

            ProjectScreenHeader(
                modifier = Modifier.fillMaxWidth(),
                projectName = projectState.project.projectName,
                onClickBackIcon = onClickIconBack,
                onClickMenuIcon = onClickIconMenu,
                isMenuOptionsVisible = projectState.isMenuOptionsVisible,
                onEditProject = { onEditProject(projectState.project.projectId) },
                onDeleteProjectClicked = onDeleteProjectClicked
            )

            Spacer(modifier = Modifier.height(Space16dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space8dp),
            ) {
                Icon(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.ic_time),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Decoration",
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(
                        id = R.string.dueDate,
                        projectState.project.projectDeadline
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                )
            }

            Spacer(modifier = Modifier.height(Space16dp))

            ProjectDescriptionSection(
                modifier = Modifier.fillMaxWidth(),
                projectDesc = projectState.project.projectDesc
            )

            Spacer(modifier = Modifier.height(Space16dp))

            when {
                projectState.milestones.isEmpty() -> {
                    EmptyMilestonesSection(
                        modifier = Modifier,
                        onAddMilestoneClicked = onAddMilestoneClicked,
                    )
                }
                else -> {
                    MilestonesSection(
                        modifier = Modifier,
                        milestones = projectState.milestones,
                        onAddMilestoneClicked = onAddMilestoneClicked,
                        selectedMilestoneStatus = projectState.selectedMilestoneStatus,
                        onClickFilterMilestoneStatus = onClickFilterMilestoneStatus,
                        onClickMilestone = onClickMilestone
                    )
                }
            }
        }

        if (projectState.isDeleteDialogVisible) {
            DeleteDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                toggleDialogVisibility = onDeleteProjectClicked,
                onDeleteProject = { onDeleteProject(projectState.project) },
                navigateBack = onClickIconBack
            )
        }
    }
}

@Composable
fun DeleteDialog(
    modifier: Modifier,
    toggleDialogVisibility: () -> Unit,
    onDeleteProject: () -> Unit,
    navigateBack: () -> Unit,
) {
    /*AlertDialog(
        modifier = modifier
            .padding(vertical = Space24dp),
        shape = RoundedCornerShape(Space36dp),
        onDismissRequest = toggleDialogVisibility,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.deleteProjectDialogTitle),
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                modifier = Modifier.padding(start = Space36dp, end = Space36dp),
                text = stringResource(id = R.string.deleteProjectDialogText),
                style = MaterialTheme.typography.bodySmall.copy(GreyNormal),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            PrimaryButton(
                modifier.fillMaxWidth(),
                text = stringResource(id = R.string.deleteProject),
                onClick = {
                    onDeleteProject()
                    toggleDialogVisibility()
                    navigateBack()
                },
                isEnabled = true)
        },
        dismissButton = {
            SecondaryButton(
                modifier.fillMaxWidth(),
                text = stringResource(id = R.string.cancel),
                onClick = toggleDialogVisibility,
                isEnabled = true)
        }
    )*/

    // TODO research a better way to add onclick outside dismiss
    // or ways to position the alert dialog at the bottom of the page

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreyDark.copy(alpha = 0.5f))
            .clickable {
                toggleDialogVisibility()
            }
    ) {
        Box(
            Modifier
                .padding(horizontal = Space20dp, vertical = Space48dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(Space36dp))
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = Space24dp, vertical = Space48dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.deleteProjectDialogTitle),
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(Space12dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.deleteProjectDialogText),
                    style = MaterialTheme.typography.bodySmall.copy(GreyNormal),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Space24dp))

                PrimaryButton(
                    modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.deleteProject),
                    onClick = {
                        onDeleteProject()
                        toggleDialogVisibility()
                        navigateBack()
                    },
                    isEnabled = true,
                    containerColor = MaterialTheme.colorScheme.onError,
                    contentColor = MaterialTheme.colorScheme.error,
                )

                Spacer(modifier = Modifier.height(Space12dp))

                SecondaryButton(
                    modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.cancel),
                    onClick = toggleDialogVisibility,
                    isEnabled = true,
                    enabledContentColor = MaterialTheme.colorScheme.outline,
                    disabledContentColor = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
fun ProjectScreenHeader(
    modifier: Modifier,
    projectName: String,
    onClickBackIcon: () -> Unit,
    onClickMenuIcon: () -> Unit,
    isMenuOptionsVisible: Boolean,
    onEditProject: () -> Unit,
    onDeleteProjectClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp, 24.dp)
                .clickable {
                    onClickBackIcon()
                },
            painter = painterResource(id = R.drawable.ic_arrow_back),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "Back button",
        )
        Text(
            text = projectName,
            style = MaterialTheme.typography.headlineLarge.copy(color = GreyDark),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .clickable {
                        onClickMenuIcon()
                    },
                painter = painterResource(id = R.drawable.ic_menu),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "menu icon",
            )
            // TODO research more on the shadows - added ones not working
            DropdownMenu(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .shadow(
                        elevation = 40.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color(0xFFCCCCCC).copy(alpha = 0.9f),
                        spotColor = Color(0xFFCCCCCC).copy(alpha = 0.9f)
                    ),
                expanded = isMenuOptionsVisible,
                onDismissRequest = onClickMenuIcon,
                offset = DpOffset(Space12dp, Space12dp),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = Space8dp),
                            text = stringResource(id = R.string.editProject),
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                        )
                    },
                    onClick = {
                        onEditProject()
                        onClickMenuIcon()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = Space8dp),
                            text = stringResource(id = R.string.deleteProject),
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                        )
                    },
                    onClick = {
                        onDeleteProjectClicked()
                        onClickMenuIcon()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectDescriptionSection(
    modifier: Modifier,
    projectDesc: String,
) {
    val descExpandedState = remember {
        mutableStateOf(false)
    }

    /**
     * TODO Read this and draw inspiration on how to best do this
     * https://blog.wolt.com/engineering/2022/04/04/expandable-text-with-read-more-action-in-android-not-an-easy-task/
     *
     * How do you hide the text button when the text is not trancated?
     * TextLayoutResult linecount didn't work
     * */

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.description),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
        )

        Spacer(modifier = Modifier.height(Space4dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = projectDesc,
            style = MaterialTheme.typography.bodySmall.copy(color = GreyNormal),
            maxLines = if (!descExpandedState.value) 3 else Int.MAX_VALUE,
            overflow = if (!descExpandedState.value) TextOverflow.Ellipsis else TextOverflow.Clip,
        )
        Text(
            modifier = Modifier
                .clickable(
                    onClick = {
                        descExpandedState.value = !descExpandedState.value
                    }
                ),
            text = if (!descExpandedState.value) stringResource(id = R.string.readMore) else stringResource(
                id = R.string.seeLess
            ),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
            maxLines = if (descExpandedState.value) 4 else Int.MAX_VALUE,
            overflow = if (descExpandedState.value) TextOverflow.Ellipsis else TextOverflow.Clip
        )
    }
}

@Composable
fun MilestonesSection(
    modifier: Modifier,
    milestones: List<Milestone>,
    onAddMilestoneClicked: () -> Unit,
    selectedMilestoneStatus: String,
    onClickMilestone: (Int) -> Unit,
    onClickFilterMilestoneStatus: (String) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space8dp),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.milestones),
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            )
            Icon(
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .clickable {
                        onAddMilestoneClicked()
                    },
                painter = painterResource(id = R.drawable.ic_add_milestone),
                contentDescription = "add new milestone",
            )
        }

        Spacer(modifier = Modifier.height(Space8dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(end = Space20dp),
            horizontalArrangement = Arrangement.spacedBy(Space8dp)
        ) {
            itemsIndexed(filters) { _, filter ->
                FilterChip(
                    text = filter,
                    selected = filter == selectedMilestoneStatus,
                    onClick = onClickFilterMilestoneStatus,
                )
            }
        }

        Spacer(modifier = Modifier.height(Space8dp))

        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(Space8dp)
        ) {
            itemsIndexed(milestones) { _, milestone ->
                MilestoneItem(
                    milestone = milestone,
                    onClickMilestone = onClickMilestone
                )
            }
        }
    }
}

@Composable
fun EmptyMilestonesSection(
    modifier: Modifier,
    onAddMilestoneClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.milestones),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
        )
        Spacer(modifier = Modifier.height(Space8dp))

        Image(
            painter = painterResource(id = R.drawable.ic_tasks_illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space12dp))

        Text(
            modifier = Modifier.padding(start = Space36dp, end = Space36dp),
            text = stringResource(id = R.string.milestoneEmptyText),
            style = MaterialTheme.typography.bodySmall.copy(GreyNormal),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space20dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.createMilestone),
            onClick = onAddMilestoneClicked,
            isEnabled = true,
        )
    }
}

@Preview
@Composable
fun ProjectPreviews() {
    ProjectTrackingTheme {

        ProjectScreenHeader(
            modifier = Modifier.fillMaxWidth(),
            projectName = "Project Name",
            onClickBackIcon = { },
            onClickMenuIcon = { },
            isMenuOptionsVisible = true,
            onEditProject = {},
            onDeleteProjectClicked = {}
        )
    }
}
