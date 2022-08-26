package com.mumbicodes.presentation.allProjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mumbicodes.R
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.util.OrderType
import com.mumbicodes.domain.util.ProjectsOrder
import com.mumbicodes.presentation.allProjects.components.FilterBottomSheetContent
import com.mumbicodes.presentation.allProjects.components.ProjectItem
import com.mumbicodes.presentation.allProjects.components.SearchBar
import com.mumbicodes.presentation.allProjects.components.StaggeredVerticalGrid
import com.mumbicodes.presentation.components.FilterChip
import com.mumbicodes.presentation.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllProjectsScreen(
    projectsViewModel: AllProjectsViewModel = hiltViewModel(),
) {
    val state = projectsViewModel.state.value
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    // Holds the user selection until they press filter
    val selectedProjectOrder =
        remember { mutableStateOf(state.projectsOrder) }

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
                    selectedProjectOrder.value = state.projectsOrder
                },
                onFiltersReset = {
                    projectsViewModel.onEvent(
                        AllProjectsEvent
                            .ResetProjectsOrder(ProjectsOrder.DateAdded(OrderType.Descending))
                    )
                },
            )
        },
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = GreyDark.copy(alpha = 0.5f)
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /*TODO Navigate to add project */ },
                    backgroundColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new project"
                    )
                }
            },
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background
        ) { padding -> // We need to pass scaffold's inner padding to content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                AllProjectsScreenContent(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 24.dp
                    ),
                    projectsState = state,
                    onClickProject = { /*TODO Navigate to project page */ },
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
                    }
                )
            }
        }
    }
}

@Composable
fun AllProjectsScreenContent(
    modifier: Modifier = Modifier,
    projectsState: AllProjectsStates,
    onClickProject: (Int) -> Unit,
    onClickFilterBtn: () -> Unit,
    onClickFilterStatus: (String) -> Unit,
) {

    Column(modifier = modifier) {
        WelcomeMessageSection(
            modifier = Modifier.fillMaxWidth(),
            projects = projectsState.projects
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchBar(
                modifier = Modifier.weight(1f),
                searchParamType = stringResource(id = R.string.projects)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(White),
                onClick = onClickFilterBtn
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter projects",
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(projectsState.filtersStatus) { _, filter ->
                FilterChip(
                    text = filter,
                    selected = filter == projectsState.selectedProjectStatus,
                    onClick = onClickFilterStatus,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredVerticalGrid(
            maxColumnWidth = 220.dp,
            modifier = Modifier.padding(4.dp)
        ) {
            projectsState.projects.forEach { project ->
                ProjectItem(
                    modifier = Modifier.padding(8.dp),
                    project = project,
                    onClickProject = onClickProject
                )
            }
        }
    }
}

@Composable
fun WelcomeMessageSection(modifier: Modifier = Modifier, projects: List<Project>) {
    Column(modifier = modifier) {

        Text(
            text = stringResource(id = R.string.greetings),
            style = MaterialTheme.typography.headlineLarge.copy(color = GreyDark),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(fontWeight = FontWeight.Normal, color = GreyNormal)
                ) {
                    append("You have ")
                }
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(textDecoration = TextDecoration.Underline, color = BlueMain)
                ) {
                    append("${projects.size}")
                }
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle()
                        .copy(fontWeight = FontWeight.Normal, color = GreyNormal)
                ) {
                    append(" projects.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AllProjectsScreenPreview() {
    ProjectTrackingTheme {
        AllProjectsScreen()
    }
}

@Preview
@Composable
fun FilterChipsPreview() {
    ProjectTrackingTheme {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            modifier = Modifier.padding(4.dp)
        ) {
            sampleDataProjects().forEach { project ->
                ProjectItem(
                    modifier = Modifier.padding(8.dp),
                    project = project,
                    onClickProject = { }
                )
            }
        }
    }
}

@Preview
@Composable
fun AllProjectsContentPreview() {
    ProjectTrackingTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            itemsIndexed(sampleDataProjects()) { _, project ->
                ProjectItem(
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
