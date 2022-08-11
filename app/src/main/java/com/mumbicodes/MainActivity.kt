package com.mumbicodes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.data.Project
import com.mumbicodes.data.sampleProjects
import com.mumbicodes.ui.theme.*
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ProjectsApp()
            }
        }
    }
}

@Composable
fun WelcomeMessages(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {

        /* Text(
            text = buildAnnotatedString {
                withStyle(ParagraphStyle(lineHeight = 44.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = TextColorDark,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                        )
                    ) {
                        append("Hello Jane!!")
                    }
                }
            }, modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )*/

        Text(
            text = "Hello Jane!!",
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
                    append("${sampleProjects().size}")
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

//TODO research on how to reduce the icon and text spacing and the whole margin

@Composable
fun SearchBar(
    modifier: Modifier = Modifier, searchParamType: String
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 60.dp,
                ambientColor = Color(0xFFCCCCCC).copy(alpha = 0.9f),
                spotColor = Color(0xFFCCCCCC).copy(alpha = 0.9f)
            ),
        shape = RoundedCornerShape(4.dp),
        color = White,
    ) {
        TextField(
            value = "",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    modifier = Modifier.alpha(0.5f),
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = GreySubtle
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = White,
                disabledTextColor = Color.Transparent,
                //Added below code to remove the underline
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f),// reduces the opacity
                    text = stringResource(id = R.string.search_placeHolder, searchParamType),
                    color = GreySubtle,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            textStyle = MaterialTheme.typography.bodySmall.copy(color = GreyNormal),

            singleLine = true,

            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(0.dp)
        )
    }

}


//TODO - Custom shadows on card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project
) {
    Card(
        //the Material color is not working - MaterialTheme.colorScheme.surface
        colors = CardDefaults.cardColors(
            containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 40.dp),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 40.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = Color(0xFFCCCCCC).copy(alpha = 0.9f),
                spotColor = Color(0xFFCCCCCC).copy(alpha = 0.9f)
            ),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.projectTitle,
                style = MaterialTheme.typography.headlineMedium.copy(color = GreyDark)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // adds three dots after 2 lines
                text = project.projectDesc,
                style = MaterialTheme.typography.labelMedium.copy(color = GreyNormal)
            )

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
                                .border(0.5.dp, BlueMain, CircleShape)
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

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = project.dueDate,
                style = MaterialTheme.typography.labelSmall.copy(color = BlueMain)
            )
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


//TODO research how to make the arrangements work in LazyVertical grid
//and make the list staggered - checkout
//https://medium.com/mobile-app-development-publication/staggeredverticalgrid-of-android-jetpack-compose-fa565e5363e1

@Composable
fun ProjectListing(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        itemsIndexed(sampleProjects()) { _, project ->
            ProjectCard(project = project)
        }
    }
}

/*

@Composable
fun ProjectsBottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = BlueAccent
    ) {

        BottomNavigationItem(
            selected = true,
            onClick = { */
/*TODO*//*
 },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = TextColorSubtle,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_filled),
                    contentDescription = null, // decorative element
                )
            })
        BottomNavigationItem(selected = false, onClick = { */
/*TODO*//*
 },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = TextColorSubtle,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tasks_outlined),
                    contentDescription = null, // decorative element
                )
            })
        BottomNavigationItem(selected = false, onClick = { */
/*TODO*//*
 },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = TextColorSubtle,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_outlined),
                    contentDescription = null, // decorative element
                )
            })
        BottomNavigationItem(selected = false, onClick = { */
/*TODO*//*
 },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = TextColorSubtle,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notifications_outlined),
                    contentDescription = null, // decorative element
                )
            })
        BottomNavigationItem(selected = false, onClick = { */
/*TODO*//*
 }, icon = {
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
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsApp(modifier: Modifier = Modifier) {
    ProjectTrackingTheme {
        Scaffold(bottomBar = { //ProjectsBottomNavigation()
             }) {

            Column(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)) {
                WelcomeMessages(
                    modifier = modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                SearchBar(searchParamType = "projects")

                Spacer(modifier = Modifier.height(8.dp))

                ProjectListing(
                    modifier
                        .fillMaxWidth()
                        .padding(it)
                )
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 200)
@Composable
fun WelcomeMessagePreview() {
    WelcomeMessages()
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun SearchBarPreview() {
    SearchBar(searchParamType = "projects")
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun ProjectListingPreview() {
    ProjectTrackingTheme {
        ProjectListing(Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun BottomNavigationPreview() {
   // ProjectsBottomNavigation()
}

@Preview(widthDp = 375, heightDp = 812)
@Composable
fun MyProjectsPreview() {
    ProjectTrackingTheme {
        ProjectsApp()
    }
}
