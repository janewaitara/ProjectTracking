package com.mumbicodes.projectie.presentation.projectDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.presentation.allProjects.filters
import com.mumbicodes.projectie.presentation.components.*
import com.mumbicodes.projectie.presentation.projectDetails.components.MilestoneItem
import com.mumbicodes.projectie.presentation.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectDetailsScreen(
    projectDetailsViewModel: ProjectDetailsViewModel = hiltViewModel(),
    onEditProject: (Int) -> Unit,
    onAddOrModifyMilestone: (Int, Int) -> Unit,
    onClickIconBack: () -> Unit,
    navigateToAllProjects: () -> Unit,
    projectId: Int? = null,
) {
    val state = projectDetailsViewModel.state.value
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val lazyListState = rememberLazyListState()

    // TODO research how to pass savedStateHandle without navigating
    LaunchedEffect(key1 = projectId) {
        projectId?.let { projectId ->
            projectDetailsViewModel.getProjectDetails(projectId = projectId)
        }
    }

    LaunchedEffect(key1 = true) {
        projectDetailsViewModel.uiEvents.collectLatest { uIEvents ->
            when (uIEvents) {
                is ProjectUIEvents.DeleteProject -> {
                    navigateToAllProjects()
                }
                is ProjectUIEvents.ShowCongratsDialog -> TODO()
                is ProjectUIEvents.DeleteMilestone -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            }
        }
    }

    // TODO add logic to restore milestone

    ModalBottomSheetLayout(
        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))

            MilestoneDetailsBottomSheetContent(
                milestoneWithTasks = state.mileStone,
                onDeleteClicked = {
                    projectDetailsViewModel.onEvent(ProjectDetailsEvents.DeleteMilestone(it))
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onModifyClicked = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                    onAddOrModifyMilestone(state.project.projectId, it)
                },
                onTaskClicked = { taskId ->
                    projectDetailsViewModel.onEvent(
                        ProjectDetailsEvents.ToggleTaskState(taskId)
                    )
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
            floatingActionButton = {
                if (state.milestones.isNotEmpty()) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.padding(
                            bottom = Space24dp, end = Space4dp,
                        ),
                        text = {
                            Text(
                                modifier = Modifier,
                                text = stringResource(
                                    id = R.string.addMilestone
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        expanded = lazyListState.isScrollingUp(),
                        icon = {
                            Icon(
                                Icons.Default.Add,
                                modifier = Modifier,
                                // painter = painterResource(id = R.drawable.ic_add_outlined),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "Decoration",
                            )
                        },
                        onClick = {
                            // Used that int because the function navigates to shared add/edit milestone
                            onAddOrModifyMilestone(state.project.projectId, -1)
                        },
                        shape = RoundedCornerShape(Space12dp)
                    )
                }
            },
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ProjectDetailsScreenContent(
                    modifier = Modifier
                        .padding(
                            top = 24.dp,
                            start = Space20dp,
                            end = Space20dp,
                        )
                        .safeContentPadding(),
                    projectState = state,
                    onClickMilestone = { milestoneId ->
                        // TODO - it might be best to pass the milestone than the Id
                        // cause we already have it - no need to get from the db again - research
                        projectDetailsViewModel.onEvent(
                            ProjectDetailsEvents.GetMilestone(milestoneId)
                        )
                        scope.launch {
                            modalBottomSheetState.show()
                        }
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
                    },
                    lazyListState = lazyListState
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
    lazyListState: LazyListState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(modifier = modifier) {

            ProjectScreenHeader(
                modifier = Modifier.fillMaxWidth(),
                onClickBackIcon = onClickIconBack,
                onClickMenuIcon = onClickIconMenu,
                isMenuOptionsVisible = projectState.isMenuOptionsVisible,
                onEditProject = { onEditProject(projectState.project.projectId) },
                onDeleteProjectClicked = onDeleteProjectClicked
            )

            Spacer(modifier = Modifier.height(Space16dp))

            Text(
                text = projectState.project.projectName,
                style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                textAlign = TextAlign.Center,
                modifier = Modifier
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

            Log.e("Milestones", projectState.milestones.toString())
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
                        milestones = projectState.filteredMilestones,
                        selectedMilestoneStatus = projectState.selectedMilestoneStatus,
                        onClickFilterMilestoneStatus = onClickFilterMilestoneStatus,
                        onClickMilestone = onClickMilestone,
                        lazyListState = lazyListState,
                        filter = projectState.selectedMilestoneStatus
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
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "Back button",
        )

        Spacer(modifier = Modifier.weight(1f))

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
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "menu icon",
            )
            // TODO research more on the shadows - added ones not working
            DropdownMenu(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .shadow(
                        elevation = 40.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = provideShadowColor(),
                        spotColor = provideShadowColor()
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
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.outline),
        )

        Spacer(modifier = Modifier.height(Space4dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = projectDesc,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.inverseSurface),
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
    milestones: List<MilestoneWithTasks>,
    selectedMilestoneStatus: String,
    onClickMilestone: (Int) -> Unit,
    onClickFilterMilestoneStatus: (String) -> Unit,
    lazyListState: LazyListState,
    filter: String,
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
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.outline),
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

        if (milestones.isEmpty()) {
            EmptyStateSection(filter = filter)
        } else {
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(Space8dp),
                state = lazyListState,
            ) {
                itemsIndexed(milestones) { _, milestone ->
                    MilestoneItem(
                        milestoneWithTasks = milestone,
                        onClickMilestone = onClickMilestone
                    )
                }
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
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.outline),
        )
        Spacer(modifier = Modifier.height(Space8dp))

        Image(
            painter = painterResource(id = R.drawable.ic_tasks_illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space36dp))

        Text(
            modifier = Modifier.padding(start = Space36dp, end = Space36dp),
            text = stringResource(id = R.string.milestoneEmptyText),
            style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space48dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.createMilestone),
            onClick = onAddMilestoneClicked,
            isEnabled = true,
        )
    }
}

@Composable
fun EmptyStateSection(
    modifier: Modifier = Modifier,
    filter: String,
) {
   /* Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(Space24dp))
        val illustration =
            when (filter) {
                "Not Started" -> R.drawable.ic_incomplete_illustration
                "In Progress" -> R.drawable.ic_inprogress_illustration
                "Completed" -> R.drawable.ic_complete_progress_illustration
                else -> R.drawable.add_project
            }

        Image(
            modifier = Modifier.height(200.dp),
            painter = painterResource(id = illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space24dp))

        val emptyText: String =
            when (filter) {
                "Not Started" -> stringResource(id = R.string.milestonesNotStartedEmptyText)
                "In Progress" -> stringResource(id = R.string.milestonesInProgressEmptyText)
                "Completed" -> stringResource(id = R.string.milestonesCompleteEmptyText)
                else -> stringResource(id = R.string.allProjectsEmptyText)
            }

        Text(
            modifier = Modifier.padding(start = Space32dp, end = Space32dp),
            text = emptyText,
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center
        )
    }*/

    val illustration: Int
    val emptyText: Int

    when (filter) {
        stringResource(id = R.string.notStarted) -> {
            illustration = R.drawable.ic_incomplete_illustration
            emptyText = R.string.milestonesNotStartedEmptyText
        }
        stringResource(id = R.string.inProgress) -> {
            illustration = R.drawable.ic_inprogress_illustration
            emptyText = R.string.milestonesInProgressEmptyText
        }
        stringResource(id = R.string.completed) -> {
            illustration = R.drawable.ic_complete_progress_illustration
            emptyText = R.string.milestonesCompleteEmptyText
        }
        else -> {
            illustration = R.drawable.add_project
            emptyText = R.string.allProjectsEmptyText
        }
    }
    EmptyStateSlot(
        modifier = modifier.fillMaxSize(),
        illustration = illustration,
        title = R.string.allMilestones,
        description = emptyText,
        titleIsVisible = false
    )
}

/**
 * Returns whether the lazy list is currently scrolling up. - gotten from one Google's codelabs -
 * https://github.com/googlecodelabs/android-compose-codelabs/blob/7a6330facd54f4f7c8d07f5fad481fb13587e422/AnimationCodelab/finished/src/main/java/com/example/android/codelab/animation/ui/home/Home.kt#L337
 */
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Preview
@Composable
fun ProjectPreviews() {
    ProjectTrackingTheme {

        ProjectScreenHeader(
            modifier = Modifier.fillMaxWidth(),
            onClickBackIcon = { },
            onClickMenuIcon = { },
            isMenuOptionsVisible = true,
            onEditProject = {},
            onDeleteProjectClicked = {}
        )
    }
}
