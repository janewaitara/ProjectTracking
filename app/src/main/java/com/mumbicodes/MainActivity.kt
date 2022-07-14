package com.mumbicodes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mumbicodes.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val title = "Portfolio"
            val desc = "The design of my personal portfolio"
            val dueDate = "12th Dec 2022"

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueAccent)
            ) {
                ProjectCard(
                    projectTitle = title,
                    projectDesc = desc,
                    painters = profiles(),
                    dueDate = dueDate
                )
            }
        }
    }
}


@Composable
fun ProjectCard(
    projectTitle: String,
    projectDesc: String,
    painters: List<Painter>,
    dueDate: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(10.dp),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = projectTitle,
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
                        append(projectDesc)
                    }
                }
            })
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement =  Arrangement.spacedBy((-8).dp)) {
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy((-8).dp)){
                //This is the same as forEach
                itemsIndexed(painters){ _, painter->
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = BluePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(dueDate)
                }
            })
        }
    }
}

@Composable
fun profiles() = listOf(
    painterResource(id = R.drawable.asian),
    painterResource(id = R.drawable.black),
    painterResource(id = R.drawable.caucasian)
)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProjectTrackingTheme {
        ProjectCard("Android", "This is the description", profiles(), "12th Dec 2022")
    }
}