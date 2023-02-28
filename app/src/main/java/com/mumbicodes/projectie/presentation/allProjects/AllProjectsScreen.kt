package com.mumbicodes.projectie.presentation.allProjects

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.presentation.activity.MainActivity
import com.mumbicodes.projectie.presentation.allProjects.components.FilterBottomSheetContent
import com.mumbicodes.projectie.presentation.allProjects.components.ProjectItem
import com.mumbicodes.projectie.presentation.allProjects.components.SearchBar
import com.mumbicodes.projectie.presentation.allProjects.components.StaggeredVerticalGrid
import com.mumbicodes.projectie.presentation.all_milestones.WelcomeMessageSection
import com.mumbicodes.projectie.presentation.components.EmptyStateSlot
import com.mumbicodes.projectie.presentation.components.ErrorStateSlot
import com.mumbicodes.projectie.presentation.components.FilterChip
import com.mumbicodes.projectie.presentation.projectDetails.EmptyStateSection
import com.mumbicodes.projectie.presentation.theme.*
import com.mumbicodes.projectie.presentation.util.ReferenceDevices
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllProjectsScreen(
    projectsViewModel: AllProjectsViewModel = hiltViewModel(),
    onClickProject: (Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val state = projectsViewModel.state.value
    val searchedTextState = projectsViewModel.searchParam.value
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    // Holds the user selection until they press filter
    val selectedProjectOrder =
        remember { mutableStateOf(state.projectsOrder) }

    BackHandler(modalBottomSheetState.isVisible) {
        scope.launch { modalBottomSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            FilterBottomSheetContent(
                projectsOrder = state.projectsOrder,
                selectedProjectsOrder = selectedProjectOrder.value,
                onOrderChange = { userProjectOrder ->
                    selectedProjectOrder.value = userProjectOrder
                },
                onFiltersApplied = {
                    projectsViewModel.onEvent(AllProjectsEvent.OrderProjects(selectedProjectOrder.value))
                    // hide the sheet after applying filters
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onFiltersReset = {
                    projectsViewModel.onEvent(
                        AllProjectsEvent.ResetProjectsOrder(ProjectsOrder.DateAdded(OrderType.Descending))
                    )
                    selectedProjectOrder.value = ProjectsOrder.DateAdded(OrderType.Descending)
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
            )
        },
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = GreyDark.copy(alpha = 0.5f)
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background
        ) { padding -> // We need to pass scaffold's inner padding to content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                AllProjectsScreenContent(
                    modifier = Modifier.padding(
                        top = 24.dp
                    ),
                    projectsState = state,
                    onClickProject = onClickProject,
                    onClickFilterBtn = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                        // TODO test whether this needs to be here
                        projectsViewModel.onEvent(AllProjectsEvent.ToggleBottomSheetVisibility)
                    },
                    onClickFilterStatus = { selectedStatus ->
                        projectsViewModel.onEvent(
                            AllProjectsEvent.SelectProjectStatus(
                                selectedStatus
                            )
                        )
                    },
                    searchedText = searchedTextState,
                    onSearchParamChanged = { searchParam ->
                        projectsViewModel.onEvent(
                            AllProjectsEvent.SearchProject(
                                searchParam
                            )
                        )
                    },
                    windowWidthSizeClass = windowWidthSizeClass
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllProjectsScreenContent(
    modifier: Modifier = Modifier,
    projectsState: AllProjectsStates,
    onClickProject: (Int) -> Unit,
    onClickFilterBtn: () -> Unit,
    onClickFilterStatus: (String) -> Unit,
    searchedText: String,
    onSearchParamChanged: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {

    if (projectsState.projects.isEmpty()) {
        EmptyStateSlot(
            illustration = R.drawable.add_project,
            title = R.string.allProjects,
            description = R.string.allProjectsEmptyText,
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
                projects = projectsState.projects
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
                    searchParamType = stringResource(id = R.string.projects),
                    searchedText = searchedText,
                    onSearchParamChanged = onSearchParamChanged
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface),
                    onClick = onClickFilterBtn,
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
                itemsIndexed(projectsState.filtersStatus) { _, filter ->
                    FilterChip(
                        text = filter,
                        selected = filter == projectsState.selectedProjectStatus,
                        onClick = onClickFilterStatus,
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space8dp))

            if (projectsState.filteredProjects.isEmpty()) {
                if (searchedText.isEmpty()) {
                    EmptyStateSection(
                        filter = projectsState.selectedProjectStatus,
                        projects = projectsState.projects
                    )
                } else {
                    ErrorStateSlot(
                        illustration = R.drawable.empty_state,
                        description = R.string.projectsErrorText,
                        searchParam = searchedText,
                        filter = projectsState.selectedProjectStatus,
                    )
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = rememberProjectsColumns(windowWidthSizeClass = windowWidthSizeClass),
                    contentPadding = PaddingValues(bottom = Space20dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Space20dp, end = Space20dp),
                    verticalArrangement = Arrangement.spacedBy(Space16dp),
                    horizontalArrangement = Arrangement.spacedBy(Space16dp)
                ) {
                    items(projectsState.filteredProjects) { project ->
                        ProjectItem(
                            // modifier = Modifier.padding(Space8dp),
                            project = project,
                            onClickProject = onClickProject
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeMessageSection(modifier: Modifier = Modifier, projects: List<Project>) {
    Column(modifier = modifier) {

        Text(
            text = stringResource(id = R.string.greetings),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
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
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                ) {
                    append("${projects.size}")
                }
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                ) {
                    append(" projects.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EmptyStateSection(
    modifier: Modifier = Modifier,
    filter: String,
    projects: List<Project>,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(Space8dp))
        val illustration =
            if (projects.isEmpty()) {
                R.drawable.add_project
            } else {
                when (filter) {
                    "Not Started" -> R.drawable.ic_incomplete_illustration
                    "In Progress" -> R.drawable.ic_inprogress_illustration
                    "Completed" -> R.drawable.ic_complete_progress_illustration
                    else -> R.drawable.add_project
                }
            }
        Image(
            painter = painterResource(id = illustration),
            contentDescription = "Empty state illustration"
        )

        Spacer(modifier = Modifier.height(Space24dp))

        val emptyText: String =
            if (projects.isEmpty()) {
                stringResource(id = R.string.allProjectsEmptyText)
            } else {
                when (filter) {
                    "Not Started" -> stringResource(id = R.string.notStartedEmptyText)
                    "In Progress" -> stringResource(id = R.string.inProgressEmptyText)
                    "Completed" -> stringResource(id = R.string.completeEmptyText)
                    else -> stringResource(id = R.string.allProjectsEmptyText)
                }
            }
        Text(
            modifier = Modifier.padding(start = Space36dp, end = Space36dp),
            text = emptyText,
            style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.inverseSurface),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberProjectsColumns(windowWidthSizeClass: WindowWidthSizeClass) =
    remember(windowWidthSizeClass) {
        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact -> StaggeredGridCells.Fixed(2)
            WindowWidthSizeClass.Medium -> StaggeredGridCells.Fixed(2)
            else -> StaggeredGridCells.Adaptive(220.dp)
        }
    }

@Preview
@Composable
fun WelcomeMessageSectionPreview() {
    ProjectTrackingTheme {
        WelcomeMessageSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space20dp),
            projects = sampleDataProjects()
        )
    }
}

@Preview
@Composable
fun FilterChipsPreview() {
    ProjectTrackingTheme {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = Space20dp),
            horizontalArrangement = Arrangement.spacedBy(Space8dp)
        ) {
            val filters = listOf(
                "All",
                "Not Started",
                "In Progress",
                "Completed",
                "Testing"
            )
            itemsIndexed(filters) { _, filter ->
                FilterChip(
                    text = filter,
                    selected = filter == "All",
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun StaggeredVerticalGridPreview() {
    ProjectTrackingTheme {
        StaggeredVerticalGrid(
            maxColumnWidth = 220.dp,
            modifier = Modifier
                .padding(horizontal = Space12dp)
                .verticalScroll(rememberScrollState())
        ) {
            sampleDataProjects().forEach { project ->
                ProjectItem(
                    modifier = Modifier.padding(Space8dp),
                    project = project,
                    onClickProject = { }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@ReferenceDevices
@Composable
fun StaggeredVerticalGridPreview2() {
    ProjectTrackingTheme {
        LazyVerticalStaggeredGrid(
            columns = rememberProjectsColumns(
                windowWidthSizeClass = calculateWindowSizeClass(activity = MainActivity()).widthSizeClass
            ),
            modifier = Modifier.padding(horizontal = Space20dp),
            contentPadding = PaddingValues(vertical = Space12dp),
            verticalArrangement = Arrangement.spacedBy(Space16dp),
            horizontalArrangement = Arrangement.spacedBy(Space16dp)
        ) {
            items(sampleDataProjects()) { project ->
                ProjectItem(
                    // modifier = Modifier.padding(Space8dp),
                    project = project,
                    onClickProject = { }
                )
            }
        }
    }
}

fun sampleDataProjects() = listOf(
    Project(
        1,
        "Portfolio",
        "The design of my personal portfolio",
        "12th Dec 2022",
        "Completed",
        12,

    ),
    Project(
        2,
        "Kalbo redesign",
        "A local tours and travel agency",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        3,
        "Notes application",
        "A multipurpose application that lets users take notes",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        4,
        "Tours and Travel",
        "A travel and tours website where",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        5,
        "Android",
        "This is the description",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        6,
        "Android and Kotlin",
        "This is the description. This is the continuation of the ",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        7,
        "Android",
        "This is the description. This is what all ",
        "12th Dec 2022",
        "Completed",
        12,
    ),
    Project(
        8,
        "Android",
        "This is the description",
        "12th Dec 2022",
        "Completed",
        12,
    ),
)
