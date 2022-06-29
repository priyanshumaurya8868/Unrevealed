package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.core.composable.UserBriefDetail
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USER
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    eventListener: (HomeScreenEvents) -> Unit,
    state: HomeScreenState,
    navController: NavController,
    openDialog: MutableState<Boolean>
) {
    val context = LocalContext.current
    val user = state.myCurrentProfile
    val menuList = listOf(
//        MenuItem(R.drawable.ic_edit, "Edit Profile"),
        MenuItem(R.drawable.ic_incognito, "My Secrets") {
            navController.navigate(
                Screen.ProfileScreen.route +
                        "?$ARG_USER=${
                            Json.encodeToString(
                                UserProfile(
                                    avatar = user.avatar,
                                    gender = user.gender,
                                    _id = user.user_id,
                                    username = user.username
                                )
                            )
                        }"
            )
        },
        MenuItem(R.drawable.ic_instagram, "Instagram"){
            context.apply{
                val uri = Uri.parse("http://instagram.com/_u/")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)

                likeIng.setPackage("com.instagram.android")

                try {
                    startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/")
                        )
                    )
                }
            }
        },
        MenuItem(R.drawable.ic_share, "Share the app") {
            context.apply {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://drive.google.com/drive/folders/14lotaZfSm9go09-tWa5u7C5NJNNTrfg7?usp=sharing"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
        },
        MenuItem(R.drawable.ic_mail, "Your Feedback") {
            context.apply {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + "priyanshumaurya8868@gmail.com")
                    )
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "")
//                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
        },
        MenuItem(R.drawable.ic_star, "Send Love"),
    )


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
                        if (state.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode
                    Icon(
                        themeIcon,
                        contentDescription = "switch to ${if (state.isDarkTheme) "Light Theme" else "Dark theme"}",
                        modifier = Modifier.noRippleClickable { eventListener(HomeScreenEvents.ToggleTheme) }
                    )
                }
            }

            item {
                CircleImage(
                    modifier = Modifier.padding(start = localSpacing),
                    image = user.avatar,
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
                    modifier = Modifier
                        .padding(horizontal = localSpacing)
                        .noRippleClickable { menuList[it].onClickListener?.let { it1 -> it1() } }
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    openDialog.value = true
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
            )
            IconWithTextDrawerMenu(
                modifier = Modifier.padding(top = localSpacing),
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
        Icon(icon, text)
        Spacer(modifier = Modifier.width(spacerWidth))
        Text(text, fontWeight = FontWeight.W400, fontSize = textSize)
    }

}




