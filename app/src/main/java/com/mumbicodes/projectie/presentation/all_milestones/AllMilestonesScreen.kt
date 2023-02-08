package com.mumbicodes.projectie.presentation.all_milestones

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextAlign
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
import com.mumbicodes.projectie.presentation.allProjects.components.SearchBar
import com.mumbicodes.projectie.presentation.allProjects.rememberProjectsColumns
import com.mumbicodes.projectie.presentation.all_milestones.components.AllMilestonesItem
import com.mumbicodes.projectie.presentation.all_milestones.components.FilterMilestonesBottomSheetContent
import com.mumbicodes.projectie.presentation.components.EmptyStateSlot
import com.mumbicodes.projectie.presentation.components.FilterChip
import com.mumbicodes.projectie.presentation.projectDetails.MilestoneDetailsBottomSheetContent
import com.mumbicodes.projectie.presentation.theme.*
import com.mumbicodes.projectie.presentation.util.ReferenceDevices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AllMilestonesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(Space24dp))
        val illustration = R.drawable.under_construction_illustration

        Image(
            modifier = Modifier.height(200.dp),
            painter = painterResource(id = illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space36dp))

        Text(
            text = stringResource(id = R.string.allMilestones),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )
        Spacer(modifier = Modifier.height(Space16dp))

        val emptyText: String = stringResource(id = R.string.coming_soon)

        Text(
            modifier = Modifier.padding(start = Space32dp, end = Space32dp),
            text = emptyText,
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllMilestonesScreens(
    allMilestonesViewModel: AllMilestonesViewModel = hiltViewModel(),
    onModifyMilestone: (Int, Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val state = allMilestonesViewModel.state.value
    val searchedTextState = allMilestonesViewModel.searchParam.value
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val bottomSheetType = remember {
        mutableStateOf(BottomSheetType.MILESTONE_DETAILS)
    }

    // Holds the user selection until they press filter - important to show user selection on radios
    val selectedMilestonesOrder = remember {
        mutableStateOf(state.milestonesOrder)
    }

    LaunchedEffect(key1 = true) {
        allMilestonesViewModel.uiEvents.collectLatest { UIEvents ->
            when (UIEvents) {
                AllMilestonesUIEvents.DeleteMilestone -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            }
        }
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
                        milestonesOrder = state.milestonesOrder,
                        selectedMilestonesOrder = selectedMilestonesOrder.value,
                        onOrderChange = { milestoneOrder ->
                            selectedMilestonesOrder.value = milestoneOrder
                        },
                        onFiltersApplied = {
                            allMilestonesViewModel.onEvent(
                                AllMilestonesEvents.OrderMilestones(
                                    selectedMilestonesOrder.value
                                )
                            )
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        onFiltersReset = {
                            allMilestonesViewModel.onEvent(
                                AllMilestonesEvents.ResetMilestonesOrder(AllMilestonesOrder.MostUrgent)
                            )
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        }
                    )
                }
                BottomSheetType.MILESTONE_DETAILS -> {
                    MilestoneDetailsBottomSheetContent(
                        milestoneWithTasks = state.mileStone,
                        onDeleteClicked = { milestone ->
                            allMilestonesViewModel.onEvent(
                                AllMilestonesEvents.DeleteMilestone(
                                    milestone
                                )
                            )
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        onModifyClicked = { milestoneId ->
                            // TODO research why the sheet still persists and I have hidden it
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            onModifyMilestone(state.mileStone.milestone.projectId, milestoneId)
                        },
                        onTaskClicked = { taskId ->
                            allMilestonesViewModel.onEvent(
                                AllMilestonesEvents.ToggleTaskState(
                                    taskId
                                )
                            )
                        },
                    )
                }
            }

            // TODO add 2 bottom sheets
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
                    milestonesStates = state,
                    onClickMilestone = { milestoneId ->
                        allMilestonesViewModel.onEvent(
                            AllMilestonesEvents.PassMilestone(
                                milestoneId = milestoneId
                            )
                        )

                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    onClickFilterBtn = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    onClickFilterStatus = { selectedFilterStatus ->
                        allMilestonesViewModel.onEvent(
                            AllMilestonesEvents.SelectMilestoneStatus(selectedFilterStatus)
                        )
                    },
                    searchedText = searchedTextState,
                    onSearchParamChanged = { searchParam ->
                        allMilestonesViewModel.onEvent(
                            AllMilestonesEvents.SearchMilestone(
                                searchParam = searchParam
                            )
                        )
                    },
                    windowWidthSizeClass = windowWidthSizeClass,
                    passBottomSheetType = { passedBottomSheetType ->
                        bottomSheetType.value = passedBottomSheetType
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllMilestonesScreenContent(
    modifier: Modifier = Modifier,
    milestonesStates: AllMilestonesStates,
    onClickMilestone: (Int) -> Unit,
    onClickFilterBtn: () -> Unit,
    onClickFilterStatus: (String) -> Unit,
    searchedText: String,
    onSearchParamChanged: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    passBottomSheetType: (BottomSheetType) -> Unit,
) {
    if (milestonesStates.milestones.isEmpty()) {
        EmptyStateSlot(
            illustration = R.drawable.add_project,
            title = R.string.allMilestones,
            description = R.string.allMilestonesEmptyText,
        )
    } else {

        Column(modifier = modifier) {

            WelcomeMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Space20dp,
                        end = Space20dp,
                    ),
                milestones = milestonesStates.milestones
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
                itemsIndexed(milestonesStates.filtersStatus) { _, filter ->
                    FilterChip(
                        text = filter,
                        selected = filter == milestonesStates.selectedMilestoneStatus,
                        onClick = onClickFilterStatus,
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space8dp))

            if (milestonesStates.filteredMilestones.isEmpty()) {
                // TODO Add an empty state
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
                    items(milestonesStates.filteredMilestones) { milestoneWithTasks ->

                        AllMilestonesItem(
                            milestoneWithTasks = milestoneWithTasks,
                            projectName = milestonesStates.milestonesProjectName[milestoneWithTasks.milestone.milestoneId]!!,
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
