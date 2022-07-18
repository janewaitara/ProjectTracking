package com.mumbicodes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mumbicodes.data.Project
import com.mumbicodes.data.sampleProjects
import com.mumbicodes.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val title = "Portfolio"
            val desc = "The design of my personal portfolio"
            val dueDate = "12th Dec 2022"

            //TODO - change background color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueAccent)
            ) {
                ProjectListing(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

//TODO research how to make the arrangements work in LazyVertical grid
//and make the list staggered

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectListing(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        itemsIndexed(sampleProjects()) { _, project ->
            ProjectCard(project = project)
        }
    }
}

//TODO - Custom shadows on card
@Composable
fun ProjectCard(
    project: Project
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.projectTitle,
                style = TextStyle(
                    TextColorDark,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
                withStyle(ParagraphStyle(lineHeight = 20.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = TextColorNormal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(project.projectDesc)
                    }
                }
            })
            Spacer(modifier = Modifier.height(8.dp))

            //testing - not even sure it will work
            project.teamMembers?.let { teamMembers ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    profilesIntListToPainter(teamMembers).forEach {
                        Image(
                            painter = it,
                            contentDescription = "User profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(0.5.dp, BluePrimary, CircleShape)
                        )
                    }
                }
            }
            /*Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                painters.forEach {
                    Image(
                        painter = it,
                        contentDescription = "User profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, BluePrimary, CircleShape)
                    )
                }
            }*/

            /*
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                //This is the same as forEach
                itemsIndexed(painters) { _, painter ->
                    Image(
                        painter = painter,
                        contentDescription = "User profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, BluePrimary, CircleShape)
                    )
                }
            }*/

            Spacer(modifier = Modifier.height(8.dp))

            Text(modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = BluePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(project.dueDate)
                }
            })
        }
    }
}


@Composable
fun profilesIntListToPainters(teamMembers: List<Int>): List<Painter> {

    val painters = mutableListOf<Painter>()

    teamMembers.forEach {
        painters.add(painterResource(id = it))
    }
    return painters
}

@Composable
fun profilesIntListToPainter(teamMembers: List<Int>): List<Painter> =
    teamMembers.map {
        painterResource(id = it)
    }

@Composable
fun projectsBottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = BlueAccent
    ) {

        BottomNavigationItem(selected = true, onClick = { /*TODO*/ }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_home_filled),
                contentDescription = null, // decorative element
            tint = BluePrimary
            )
        })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tasks_outlined),
                contentDescription = null, // decorative element
                        tint = TextColorSubtle
            )
        })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_outlined),
                contentDescription = null, // decorative element
                tint = TextColorSubtle
            )
        })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_notifications_outlined),
                contentDescription = null, // decorative element
                tint = TextColorSubtle
            )
        })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Image(
                painter = painterResource(id = R.drawable.caucasian),
                contentDescription = "User profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)

            )
        })
    }
}

@Composable
fun ProjectsApp(modifier: Modifier = Modifier) {
    ProjectTrackingTheme {
        Scaffold(bottomBar = { projectsBottomNavigation() }) {
            ProjectListing(
                Modifier
                    .fillMaxWidth()
                    .padding(it)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun DefaultPreview() {
    ProjectTrackingTheme {
        ProjectListing(Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun BottomNavigationPreview() {
    projectsBottomNavigation()
}


@Preview(widthDp = 360, heightDp = 640)
@Composable
fun MyProjectsPreview() {
    ProjectsApp()
}
