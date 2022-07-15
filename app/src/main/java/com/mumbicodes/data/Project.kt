package com.mumbicodes.data

import com.mumbicodes.R

data class Project(
    var projectTitle: String,
    var projectDesc: String,
    var teamMembers: List<Int>? = null,
    var dueDate: String
)

fun sampleProjects() = listOf(
    Project(
        "Portfolio",
        "The design of my personal portfolio",
        imageResources,
        "12th Dec 2022"
    ),
    Project(
        "Kalbo redesign",
        "A local tours and travel agency",
        imageResources,
        "12th Dec 2022"
    ),
    Project(
        projectTitle =  "Notes application",
        projectDesc ="A multipurpose application that lets users take notes",
        dueDate = "12th Dec 2022"
    ),
    Project(
        "Tours and Travel",
        "A travel and tours website where",
        imageResources,
        "12th Dec 2022"
    ),
    Project(
        projectTitle =  "Android",
        projectDesc ="This is the description",
        dueDate = "12th Dec 2022"
    ),
    Project(
        "Android",
        "This is the description",
        imageResources,
        "12th Dec 2022"
    ),
    Project(
        "Android",
        "This is the description",
        imageResources,
        "12th Dec 2022"
    ),
    Project(
        "Android",
        "This is the description",
        imageResources,
        "12th Dec 2022"
    ),
)

val imageResources = listOf(
    R.drawable.asian,
    R.drawable.black,
    R.drawable.caucasian
)

