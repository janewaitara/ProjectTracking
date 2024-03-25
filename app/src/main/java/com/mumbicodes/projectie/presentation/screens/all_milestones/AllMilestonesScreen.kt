package com.mumbicodes.projectie.presentation.screens.all_milestones

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.model.Task
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import com.mumbicodes.projectie.domain.util.AllMilestonesOrder
import com.mumbicodes.projectie.presentation.activity.MainActivity
import com.mumbicodes.projectie.presentation.designsystem.components.EmptyStateSlot
import com.mumbicodes.projectie.presentation.designsystem.components.ErrorStateSlot
import com.mumbicodes.projectie.presentation.designsystem.components.FilterChip
import com.mumbicodes.projectie.presentation.designsystem.components.ShimmerEffectComposable
import com.mumbicodes.projectie.presentation.designsystem.theme.GreyDark
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space20dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space24dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space8dp
import com.mumbicodes.projectie.presentation.screens.allProjects.components.SearchBar
import com.mumbicodes.projectie.presentation.screens.allProjects.rememberProjectsColumns
import com.mumbicodes.projectie.presentation.screens.all_milestones.components.AllMilestonesItem
import com.mumbicodes.projectie.presentation.screens.all_milestones.components.FilterMilestonesBottomSheetContent
import com.mumbicodes.projectie.presentation.screens.projectDetails.MilestoneDetailsBottomSheetContent
import com.mumbicodes.projectie.presentation.util.ReferenceDevices
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AllMilestonesScreenRoute(
    allMilestonesViewModel: AllMilestonesViewModel = hiltViewModel(),
    onModifyMilestone: (Int, Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val screenState = allMilestonesViewModel.screenStates.value
    val uiEvents = allMilestonesViewModel.uiEvents

    AllMilestonesScreen(
        screenState = screenState,
        uiEvents = uiEvents,
        onModifyMilestone = onModifyMilestone,
        onFiltersApplied = {
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.OrderMilestones
            )
        },
        onFiltersReset = {
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.ResetMilestonesOrder(AllMilestonesOrder.MostUrgent)
            )
        },
        onDeleteClicked = { milestone ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.DeleteMilestone(
                    milestone
                )
            )
        },
        onTaskClicked = { taskId ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.ToggleTaskState(
                    taskId
                )
            )
        },
        onClickMilestone = { milestoneId ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.PassMilestone(
                    milestoneId = milestoneId
                )
            )
        },
        onClickFilterStatus = { selectedFilterStatus ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.SelectMilestoneStatus(selectedFilterStatus)
            )
        },
        onSearchParamChanged = { searchParam ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.SearchMilestone(
                    searchParam = searchParam
                )
            )
        },
        onMilestoneOrderUpdated = { milestoneOrder ->
            allMilestonesViewModel.onEvent(
                AllMilestonesEvents.UpdateMilestoneOrder(
                    milestoneOrder
                )
            )
        },
        windowWidthSizeClass = windowWidthSizeClass,

    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllMilestonesScreen(
    screenState: ScreenStates,
    uiEvents: MutableSharedFlow<AllMilestonesUIEvents>,
    onModifyMilestone: (Int, Int) -> Unit,
    onFiltersApplied: () -> Unit,
    onFiltersReset: () -> Unit,
    onDeleteClicked: (Milestone) -> Unit,
    onTaskClicked: (Int) -> Unit = {},
    onClickMilestone: (Int) -> Unit,
    onClickFilterStatus: (String) -> Unit,
    onSearchParamChanged: (String) -> Unit,
    onMilestoneOrderUpdated: (AllMilestonesOrder) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val bottomSheetType = remember {
        mutableStateOf(BottomSheetType.MILESTONE_DETAILS)
    }

    LaunchedEffect(key1 = true) {
        uiEvents.collectLatest { UIEvents ->
            when (UIEvents) {
                is AllMilestonesUIEvents.DeleteMilestone -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            }
        }
    }

    BackHandler(modalBottomSheetState.isVisible) {
        scope.launch { modalBottomSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            /***
             * the spacer solves the error
             * The initial value must have an associated anchor.
             * */

            Spacer(modifier = Modifier.height(1.dp))
            when (bottomSheetType.value) {
                BottomSheetType.FILTER -> {
                    FilterMilestonesBottomSheetContent(
                        milestonesOrder = screenState.data.milestonesOrder,
                        selectedMilestonesOrder = screenState.data.selectedMilestoneOrder,
                        onOrderChange = onMilestoneOrderUpdated,
                        onFiltersApplied = {
                            onFiltersApplied()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        onFiltersReset = {
                            onFiltersReset()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        }
                    )
                }

                BottomSheetType.MILESTONE_DETAILS -> {
                    MilestoneDetailsBottomSheetContent(
                        milestoneWithTasks = screenState.data.mileStone,
                        onDeleteClicked = { milestone ->
                            onDeleteClicked(milestone)
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        onModifyClicked = { milestoneId ->
                            // TODO research why the sheet still persists and I have hidden it
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            onModifyMilestone(
                                screenState.data.mileStone.milestone.projectId,
                                milestoneId
                            )
                        },
                        onTaskClicked = onTaskClicked,
                    )
                }
            }
        },
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(topStart = Space16dp, topEnd = Space16dp),
        scrimColor = GreyDark.copy(alpha = 0.5f),
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AllMilestonesScreenContent(
                    modifier = Modifier.padding(
                        top = 24.dp
                    ),
                    milestonesStates = screenState,
                    onClickMilestone = { milestoneId ->
                        onClickMilestone(milestoneId)

                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    onClickFilterBtn = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    onClickFilterStatus = onClickFilterStatus,
                    searchedText = screenState.data.searchParam,
                    onSearchParamChanged = onSearchParamChanged,
                    windowWidthSizeClass = windowWidthSizeClass,
                    passBottomSheetType = { passedBottomSheetType ->
                        bottomSheetType.value = passedBottomSheetType
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllMilestonesScreenContent(
    modifier: Modifier = Modifier,
    milestonesStates: ScreenStates,
    onClickMilestone: (Int) -> Unit,
    onClickFilterBtn: () -> Unit,
    onClickFilterStatus: (String) -> Unit,
    searchedText: String,
    onSearchParamChanged: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    passBottomSheetType: (BottomSheetType) -> Unit,
) {

    if (milestonesStates.data.milestones.isEmpty()) {
        if (milestonesStates.isLoading) {
            ShimmerEffectComposable()
        } else {
            EmptyStateSlot(
                illustration = R.drawable.add_project,
                title = R.string.allMilestones,
                description = R.string.allMilestonesEmptyText,
            )
        }
    } else {

        Column(modifier = modifier) {

            WelcomeMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Space20dp,
                        end = Space20dp,
                    ),
                milestones = milestonesStates.data.milestones
            )
            Spacer(modifier = Modifier.height(Space24dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Space20dp,
                        end = Space20dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    searchParamType = stringResource(id = R.string.milestones).lowercase(Locale.getDefault()),
                    searchedText = searchedText,
                    onSearchParamChanged = onSearchParamChanged
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface),
                    onClick = {
                        onClickFilterBtn()

                        passBottomSheetType(
                            BottomSheetType.FILTER
                        )
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter projects",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space16dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = Space20dp),
                horizontalArrangement = Arrangement.spacedBy(Space8dp)
            ) {
                itemsIndexed(milestonesStates.data.filtersStatus) { _, filter ->
                    FilterChip(
                        text = filter,
                        selected = filter == milestonesStates.data.selectedMilestoneStatus,
                        onClick = onClickFilterStatus,
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space8dp))

            if (milestonesStates.data.filteredMilestones.isEmpty()) {
                if (searchedText.isEmpty()) {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = Space20dp, end = Space20dp),
                        filter = milestonesStates.data.selectedMilestoneStatus,
                    )
                } else {
                    ErrorStateSlot(
                        illustration = R.drawable.empty_state,
                        description = R.string.milestonesErrorText,
                        searchParam = searchedText,
                        filter = milestonesStates.data.selectedMilestoneStatus,
                    )
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = rememberAllMilestonesColumns(windowWidthSizeClass = windowWidthSizeClass),
                    contentPadding = PaddingValues(bottom = Space20dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Space20dp, end = Space20dp),
                    verticalArrangement = Arrangement.spacedBy(Space16dp),
                    horizontalArrangement = Arrangement.spacedBy(Space16dp)
                ) {
                    items(milestonesStates.data.filteredMilestones) { milestoneWithTasks ->

                        AllMilestonesItem(
                            milestoneWithTasks = milestoneWithTasks,
                            projectName = milestonesStates.data.milestonesProjectName[milestoneWithTasks.milestone.milestoneId]
                                ?: "No project name",
                            onClickMilestone = {
                                onClickMilestone(it)

                                passBottomSheetType(
                                    BottomSheetType.MILESTONE_DETAILS
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeMessageSection(
    modifier: Modifier = Modifier,
    milestones: List<MilestoneWithTasks>,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.allMilestones),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )
        Spacer(modifier = Modifier.height(Space8dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                ) {
                    append("You have ")
                }
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {
                    append("${milestones.size}")
                }
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                ) {
                    append(" milestones.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EmptyState(
    modifier: Modifier,
    filter: String,
) {
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
        modifier = modifier,
        illustration = illustration,
        title = R.string.allMilestones,
        description = emptyText,
        titleIsVisible = false
    )
}

enum class BottomSheetType {
    FILTER, MILESTONE_DETAILS
}

@Preview
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@ReferenceDevices
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AllMilestonesPreviewDark() {
    ProjectTrackingTheme() {
        LazyVerticalStaggeredGrid(
            columns = rememberProjectsColumns(
                windowWidthSizeClass = calculateWindowSizeClass(activity = MainActivity()).widthSizeClass
            ),
            contentPadding = PaddingValues(bottom = Space20dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = Space20dp, end = Space20dp),
            verticalArrangement = Arrangement.spacedBy(Space16dp),
            horizontalArrangement = Arrangement.spacedBy(Space16dp)
        ) {
            items(sampleDataMilestones()) { milestoneWithTasks ->
                AllMilestonesItem(
                    milestoneWithTasks = milestoneWithTasks,
                    projectName = "Project name",
                    onClickMilestone = { }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAllMilestonesColumns(windowWidthSizeClass: WindowWidthSizeClass) =
    remember(windowWidthSizeClass) {
        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact -> StaggeredGridCells.Fixed(1)
            WindowWidthSizeClass.Medium -> StaggeredGridCells.Fixed(2)
            else -> StaggeredGridCells.Adaptive(220.dp)
        }
    }

fun sampleDataMilestones() = listOf(
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
    MilestoneWithTasks(
        Milestone(
            projectId = 1,
            milestoneId = 2,
            milestoneTitle = "This is a milestone title",
            milestoneSrtDate = 19236,
            milestoneEndDate = 19247,
            status = "Not started",
        ),
        tasks = listOf(
            Task(
                taskId = 1,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
            Task(
                taskId = 2,
                milestoneId = 2,
                taskTitle = "Bottom navigation",
                taskDesc = "Bottom navigation",
                status = false
            ),
            Task(
                taskId = 3,
                milestoneId = 2,
                taskTitle = "Display Projects",
                taskDesc = "Display Projects",
                status = true
            ),
        )
    ),
)
