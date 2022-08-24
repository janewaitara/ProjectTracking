package com.mumbicodes.presentation.allProjects.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mumbicodes.R
import com.mumbicodes.presentation.theme.GreyNormal
import com.mumbicodes.presentation.theme.GreySubtle
import com.mumbicodes.presentation.theme.ProjectTrackingTheme
import com.mumbicodes.presentation.theme.White

// TODO research on how to reduce the icon and text spacing and the whole margin

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchParamType: String
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
                // Added below code to remove the underline
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f), // reduces the opacity
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

@Preview
@Composable
fun SearchItemPreview() {
    ProjectTrackingTheme {
        SearchBar(searchParamType = "projects")
    }
}
