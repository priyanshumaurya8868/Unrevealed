package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
@ExperimentalFoundationApi
fun AvatarGridView(
    modifier: Modifier = Modifier,
    list: List<String>,
    selectedImgCallback: (String) -> Unit
) {

    var selectedIndex by remember {
        mutableStateOf(-1)
    }

    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = localSpacing, vertical = localVerticalSpacing),
        columns =  GridCells.Fixed(3)
    ) {
        items(list.size) {
            Card(
                backgroundColor = if (selectedIndex == it) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                shape = CircleShape,
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    painter = rememberImagePainter(list[it]),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp, end = 5.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            selectedIndex = it
                            selectedImgCallback(list[it])
                        }

                )
            }

        }
    }
}


