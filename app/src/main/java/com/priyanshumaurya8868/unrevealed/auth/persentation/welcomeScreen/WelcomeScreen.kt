package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.AppTitle
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables.BottomEndButton
import com.priyanshumaurya8868.unrevealed.core.Screen

val localSpacing = 20.dp
val localVerticalSpacing = 10.dp
val fontSize_1 = 18.sp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel= hiltViewModel()
) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colors.background
    )

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(localSpacing),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppTitle()
                Column {
                    Image(
                        painter = if (viewModel.themeSwitcher.IS_DARK_THEME) painterResource(id = R.drawable.cover_dark)
                        else painterResource(id = R.drawable.cover_ligth),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                    )
                    Text(
                        "Reveal What's Unrevealed!",
                        fontWeight = FontWeight.W500,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Few things are really heavy to bear. " +
                                "Read the deepest darkest secrets stories of people around the world. " +
                                "Share yours being anonymous.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End,
            ) {
                BottomEndButton(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        navController.navigate(Screen.AuthOptionsScreen.route)
                    },
                    text = "Start",
                    endIcon = Icons.Default.ArrowForward,
                    fontSize = fontSize_1
                )
            }
        }

    }
}



