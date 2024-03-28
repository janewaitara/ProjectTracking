package com.mumbicodes.projectie.presentation.screens.allProjects

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.util.OrderType
import com.mumbicodes.projectie.domain.util.ProjectsOrder
import com.mumbicodes.projectie.presentation.activity.MainActivity
import com.mumbicodes.projectie.presentation.designsystem.components.EmptyStateSlot
import com.mumbicodes.projectie.presentation.designsystem.components.ErrorStateSlot
import com.mumbicodes.projectie.presentation.designsystem.components.NotificationsAlertComposable
import com.mumbicodes.projectie.presentation.designsystem.components.ShimmerEffectComposable
import com.mumbicodes.projectie.presentation.designsystem.theme.GreyDark
import com.mumbicodes.projectie.presentation.designsystem.theme.ProjectTrackingTheme
import com.mumbicodes.projectie.presentation.designsystem.theme.Space12dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space16dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space20dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space24dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space36dp
import com.mumbicodes.projectie.presentation.designsystem.theme.Space8dp
import com.mumbicodes.projectie.presentation.screens.allProjects.components.FilterBottomSheetContent
import com.mumbicodes.projectie.presentation.screens.allProjects.components.ProjectItem
import com.mumbicodes.projectie.presentation.screens.allProjects.components.SearchBar
import com.mumbicodes.projectie.presentation.screens.allProjects.components.StaggeredVerticalGrid
import com.mumbicodes.projectie.presentation.screens.all_milestones.WelcomeMessageSection
import com.mumbicodes.projectie.presentation.util.ReferenceDevices
import com.mumbicodes.projectie.presentation.util.state.ListState
import com.mumbicodes.projectie.presentation.util.state.ScreenState
import com.mumbicodes.projectie.presentation.util.state.SuccessState
import kotlinx.coroutines.launch

@Composable
fun AllProjectsScreenRoute(
    projectsViewModel: AllProjectsViewModel = hiltViewModel(),
    onClickProject: (Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val state = projectsViewModel.state.value

    AllProjectsScreen(
        state = state,
        onFiltersApplied = {
            projectsViewModel.onEvent(
                AllProjectsEvent.OrderProjects
            )
        },
        onFiltersClicked = {
            projectsViewModel.onEvent(AllProjectsEvent.UpdateProjectOrder(it))
        },
        onFiltersReset = {
            projectsViewModel.onEvent(
                AllProjectsEvent.ResetProjectsOrder(ProjectsOrder.DateAdded(OrderType.Descending))
            )
        },
        onClickProject = onClickProject,
        onClickNotBtn = {
            projectsViewModel.saveNotPromptState(it)
        },
        onSearchParamChanged = { searchParam ->
            projectsViewModel.onEvent(
                AllProjectsEvent.SearchProject(
                    searchParam
                )
            )
        },
        onClickFilterStatus = { selectedStatus ->
            projectsViewModel.onEvent(
                AllProjectsEvent.SelectProjectStatus(
                    selectedStatus
                )
            )
        },
        onClickFilterBtn = {
            projectsViewModel.onEvent(AllProjectsEvent.ToggleBottomSheetVisibility)
        },
        windowWidthSizeClass = windowWidthSizeClass,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllProjectsScreen(
    modifier: Modifier = Modifier,
    state: ScreenState<AllProjectsStates>,
    onFiltersApplied: () -> Unit,
    onFiltersClicked: (ProjectsOrder) -> Unit,
    onFiltersReset: () -> Unit,
    onClickProject: (Int) -> Unit,
    onClickNotBtn: (Boolean) -> Unit,
    onSearchParamChanged: (String) -> Unit,
    onClickFilterStatus: (String) -> Unit,
    onClickFilterBtn: () -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    BackHandler(modalBottomSheetState.isVisible) {
        scope.launch { modalBottomSheetState.hide() }
    }

    when (state) {
        ScreenState.Loading -> {
            ShimmerEffectComposable()
        }

        ScreenState.Empty -> {
            EmptyStateSlot(
                illustration = R.drawable.add_project,
                title = R.string.allProjects,
                description = R.string.allProjectsEmptyText,
            )
        }

        is ScreenState.Data -> {
            ModalBottomSheetLayout(
                sheetContent = {
                    FilterBottomSheetContent(
                        projectsOrder = (state as ScreenState.Data).data.projectsOrder,
                        selectedProjectsOrder = state.data.selectedProjectOrder,
                        onOrderChange = onFiltersClicked,
                        onFiltersApplied = {
                            onFiltersApplied()
                            // hide the sheet after applying filters
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        onFiltersReset = {
                            onFiltersReset()
                            onFiltersClicked(ProjectsOrder.DateAdded(OrderType.Descending))
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
                            projectsScreenState = state,
                            onClickProject = onClickProject,
                            onClickFilterBtn = {
                                scope.launch {
                                    modalBottomSheetState.show()
                                }
                                // TODO test whether this needs to be here
                                onClickFilterBtn()
                            },
                            onClickFilterStatus = onClickFilterStatus,
                            onSearchParamChanged = onSearchParamChanged,
                            windowWidthSizeClass = windowWidthSizeClass,
                            onClickNotBtn = onClickNotBtn
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllProjectsScreenContent(
    modifier: Modifier = Modifier,
    projectsScreenState: ScreenState.Data<AllProjectsStates>,
    onClickProject: (Int) -> Unit,
    onClickFilterBtn: () -> Unit,
    onClickFilterStatus: (String) -> Unit,
    onSearchParamChanged: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    onClickNotBtn: (Boolean) -> Unit,
) {

    Column(modifier = modifier) {
        WelcomeMessageSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Space20dp,
                    end = Space20dp,
                ),
            projects = ((projectsScreenState.data.projects as ListState.Success).data as SuccessState.Data).data
        )
        Spacer(modifier = Modifier.height(Space24dp))

        RequestNotifications(
            hasRequestedNotificationPermission = projectsScreenState.data.hasRequestedNotificationPermission,
            onClickNotBtn = onClickNotBtn
        )

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
                searchedText = projectsScreenState.data.searchParam,
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
            itemsIndexed(projectsScreenState.data.filtersStatus) { _, filter ->
                com.mumbicodes.projectie.presentation.designsystem.components.FilterChip(
                    text = filter,
                    selected = filter == projectsScreenState.data.selectedProjectStatus,
                    onClick = onClickFilterStatus,
                )
            }
        }

        Spacer(modifier = Modifier.height(Space8dp))

        when (val filteredProjects = projectsScreenState.data.filteredProjects) {
            is ListState.Error -> TODO()
            ListState.Loading -> TODO()
            is ListState.Success -> {
                when (filteredProjects.data) {
                    is SuccessState.Data -> {
                        Log.d(" Success", "With Data")
                        LazyVerticalStaggeredGrid(
                            columns = rememberProjectsColumns(windowWidthSizeClass = windowWidthSizeClass),
                            contentPadding = PaddingValues(bottom = Space20dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = Space20dp, end = Space20dp),
                            verticalArrangement = Arrangement.spacedBy(Space16dp),
                            horizontalArrangement = Arrangement.spacedBy(Space16dp)
                        ) {
                            items(filteredProjects.data.data) { project ->
                                ProjectItem(
                                    // modifier = Modifier.padding(Space8dp),
                                    project = project,
                                    onClickProject = onClickProject
                                )
                            }
                        }
                    }

                    SuccessState.Empty -> {
                        Log.d(" Success", "Empty")
                        if (projectsScreenState.data.searchParam.isEmpty()) {
                            EmptyStateSection(
                                filter = projectsScreenState.data.selectedProjectStatus,
                                projects = (projectsScreenState.data.projects.data as SuccessState.Data).data
                            )
                        } else {
                            ErrorStateSlot(
                                illustration = R.drawable.empty_state,
                                description = R.string.projectsErrorText,
                                searchParam = projectsScreenState.data.searchParam,
                                filter = projectsScreenState.data.selectedProjectStatus,
                            )
                        }
                    }
                }
            }
        }
    }
    /*
    *    if (projectsScreenState.data.filteredProjects.isEmpty()) {
                if (searchedText.isEmpty()) {
                    EmptyStateSection(
                        filter = projectsScreenState.data.selectedProjectStatus,
                        projects = projectsScreenState.data.projects
                    )
                } else {
                    ErrorStateSlot(
                        illustration = R.drawable.empty_state,
                        description = R.string.projectsErrorText,
                        searchParam = searchedText,
                        filter = projectsScreenState.data.selectedProjectStatus,
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
                    items(projectsScreenState.data.filteredProjects) { project ->
                        ProjectItem(
                            // modifier = Modifier.padding(Space8dp),
                            project = project,
                            onClickProject = onClickProject
                        )
                    }
                }
            }
    * */

    /*if (projectsScreenState.data.projects.isEmpty()) {
        if (projectsScreenState.isLoading) {
            ShimmerEffectComposable()
        } else {
            EmptyStateSlot(
                illustration = R.drawable.add_project,
                title = R.string.allProjects,
                description = R.string.allProjectsEmptyText,
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
                projects = projectsScreenState.data.projects
            )
            Spacer(modifier = Modifier.height(Space24dp))

            RequestNotifications(
                hasRequestedNotificationPermission = projectsScreenState.data.hasRequestedNotificationPermission,
                onClickNotBtn = onClickNotBtn
            )

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
                itemsIndexed(projectsScreenState.data.filtersStatus) { _, filter ->
                    com.mumbicodes.projectie.presentation.designsystem.components.FilterChip(
                        text = filter,
                        selected = filter == projectsScreenState.data.selectedProjectStatus,
                        onClick = onClickFilterStatus,
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space8dp))

            if (projectsScreenState.data.filteredProjects.isEmpty()) {
                if (searchedText.isEmpty()) {
                    EmptyStateSection(
                        filter = projectsScreenState.data.selectedProjectStatus,
                        projects = projectsScreenState.data.projects
                    )
                } else {
                    ErrorStateSlot(
                        illustration = R.drawable.empty_state,
                        description = R.string.projectsErrorText,
                        searchParam = searchedText,
                        filter = projectsScreenState.data.selectedProjectStatus,
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
                    items(projectsScreenState.data.filteredProjects) { project ->
                        ProjectItem(
                            // modifier = Modifier.padding(Space8dp),
                            project = project,
                            onClickProject = onClickProject
                        )
                    }
                }
            }
        }
    }*/
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
fun RequestNotifications(
    hasRequestedNotificationPermission: Boolean,
    onClickNotBtn: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    Log.e("Permission granted", hasNotificationPermission.toString())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (!hasRequestedNotificationPermission && !hasNotificationPermission) {
            NotificationsAlertComposable(
                modifier = Modifier
                    .padding(
                        start = Space20dp,
                        end = Space20dp,
                    ),
                onClick = {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                    // TODO research how to this after the launcher Modal is dismissed
                    onClickNotBtn(!hasNotificationPermission)
                }
            )

            Spacer(modifier = Modifier.height(Space24dp))
        }
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
                com.mumbicodes.projectie.presentation.designsystem.components.FilterChip(
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
