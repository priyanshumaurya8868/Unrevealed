package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.Profiler
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.menuList
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.toolbarHeight


@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    user: UserProfile,
    eventListener: (HomeScreenEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        LazyColumn(
            modifier = Modifier.padding(horizontal = localSpacing),
            contentPadding = PaddingValues(
                top = localSpacing,
                start = localVerticalSpacing
            )
        ) {
            item {
                Spacer(modifier = Modifier.height(toolbarHeight.plus(localVerticalSpacing)))
                Profiler(
                    modifier = Modifier.fillMaxWidth(),
                    image = rememberImagePainter(user.avatar),
                    username = user.username,
                    gender = user.gender
                )
            }

            items(menuList.size) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(elevation = 5.dp, shape = RoundedCornerShape(5.dp)) {
                        Icon(
                            painterResource(id = menuList[it].iconId),
                            menuList[it].title,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.secondary.copy(alpha = .6f)
                        )
                    }
                    Spacer(modifier = Modifier.width(localSpacing))
                    Text(
                        text = menuList[it].title,
                        fontWeight = FontWeight.W400,
                        fontSize = fontSize_1,

                        )
                }
                Spacer(modifier = Modifier.height(if (it == 1) localSpacing else localVerticalSpacing))
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                eventListener(HomeScreenEvents.LogOutUser)
            }) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = localSpacing.times(2), vertical = localSpacing)
            ) {
                val color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                Icon(Icons.Default.Logout, contentDescription = "logout", tint = color)
                Spacer(modifier = modifier.width(localSpacing))
                Text(text = "Logout", fontSize = fontSize_1, color = color)
            }
        }

    }
}
