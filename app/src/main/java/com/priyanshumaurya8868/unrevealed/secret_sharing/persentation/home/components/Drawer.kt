package com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.Profiler
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.toolbarHeight
import com.priyanshumaurya8868.unrevealed.utils.Constants


@Composable
fun Drawer(modifier: Modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = localSpacing)
    ) {
        Spacer(modifier = Modifier.height(toolbarHeight.plus(localVerticalSpacing)))
        Profiler(
            modifier = Modifier.fillMaxWidth(),
            image = painterResource(id = R.drawable.female_10),
            username = "DreamShade",
            gender = "Female"
        )

        LazyColumn(
            contentPadding = PaddingValues(
                top = localSpacing,
                start = localVerticalSpacing
            )
        ) {
            items(Constants.menuList.size) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(elevation = 5.dp, shape = RoundedCornerShape(5.dp)) {
                        Icon(
                            painterResource(id = Constants.menuList[it].iconId),
                            Constants.menuList[it].title,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.secondary.copy(alpha = .6f)
                        )
                    }
                    Spacer(modifier = Modifier.width(localSpacing))
                    Text(
                        text = Constants.menuList[it].title,
                        fontWeight = FontWeight.W400,
                        fontSize = fontSize_1,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(if (it == 1) localSpacing else localVerticalSpacing))
            }
        }


    }
}
