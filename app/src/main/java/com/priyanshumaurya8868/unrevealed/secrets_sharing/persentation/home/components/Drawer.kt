package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import android.webkit.WebSettings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.core.composable.UserBriefDetail
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.menuList


@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    eventListener: (HomeScreenEvents) -> Unit,
    state: HomeScreenState,
    navController: NavController
) {
    val user = state.myCurrentProfile
    Column(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing),
                    horizontalArrangement = Arrangement.End
                ) {
                    val themeIcon =
                        if (isSystemInDarkTheme()) Icons.Default.DarkMode else Icons.Default.LightMode
                    Icon(
                        themeIcon,
                        contentDescription = "switch to ${if (isSystemInDarkTheme()) "Light Theme" else "Dark theme"}",
                        modifier = Modifier.noRippleClickable { eventListener(HomeScreenEvents.ToggleTheme) }
                    )
                }
            }

            item {
                CircleImage(
                    modifier = Modifier.padding(start = localSpacing),
                    image = rememberImagePainter(user.avatar),
                    size = 80.dp
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UserBriefDetail(username = user.username, gender = user.gender)
                    val degree: Float by animateFloatAsState(if (state.isLoggedUsersListExpanded) 1f else 0f)
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = if (state.isLoggedUsersListExpanded) "hide Accounts" else "see accounts",
                        modifier = Modifier
                            .noRippleClickable { eventListener(HomeScreenEvents.ToggleListOfLoggedUSer) }
                            .rotate(180f * degree)
                    )
                }
            }


            item {
                if (state.isLoggedUsersListExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = localSpacing, start = localSpacing, end = localSpacing)
                    ) {
                        state.loggerUsers.forEachIndexed { _, myProfile ->
                            ProfileItem(
                                textColor = MaterialTheme.colors.onBackground,
                                profile = myProfile,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(bottom = localVerticalSpacing)
                                    .noRippleClickable {
                                        eventListener(HomeScreenEvents.SwitchAccount(myProfile))
                                    },
                                isSelected = myProfile == state.myCurrentProfile
                            )
                        }
                        IconWithTextDrawerMenu(
                            icon = Icons.Default.Add,
                            text = "Add Accounts",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = localVerticalSpacing,
                                    start = localVerticalSpacing.div(2)
                                )
                                .clickable {
                                    navController.navigate(
                                        Screen.LoginScreen.route
                                    )
                                }
                        )

                    }
                }
            }

            item {
               Spacer(modifier = Modifier.height(localSpacing))
            }

            items(menuList.size) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = localSpacing)
                ) {
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
            .height(60.dp)
            .clickable {
                eventListener(HomeScreenEvents.LogOutUser)
            },
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
            )
            IconWithTextDrawerMenu(
                modifier = Modifier.padding( top = localSpacing),
                icon = Icons.Default.Logout,
                text = "Logout"
            )
        }

    }
}

@Composable
fun IconWithTextDrawerMenu(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    spacerWidth: Dp = localSpacing,
    textSize: TextUnit = fontSize_1
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, text,  )
        Spacer(modifier = Modifier.width(spacerWidth))
        Text(text, fontWeight = FontWeight.W400, fontSize = textSize)
    }

}




