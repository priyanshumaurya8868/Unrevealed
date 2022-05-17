package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets.AccountsItem
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets.ChoiceMakingButtons
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.AppTitle
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.WelcomeScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthOptionsScreen(
    navController: NavController,
    viewModel: AuthOptionViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true ){
        viewModel.eventFlow.collectLatest{event->
            when(event){
                is AuthOptionViewModel.UiEvent.Proceed->{
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.WelcomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    if (viewModel.users.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(localSpacing),
            contentAlignment = Alignment.Center
        ) {
            ChoiceMakingButtons(
                modifier = Modifier.fillMaxWidth(),
                btnText1 = "Create New Account",
                btn1OnClick = {
                    navController.navigate(Screen.SignupScreen.route)
                },
                btnText2 = "Login",
                btn2OnClick = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(localSpacing),
                contentPadding = PaddingValues( bottom = localSpacing.times(5))
            ) {
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(localVerticalSpacing),
                    horizontalArrangement = Arrangement.Center) {
                        AppTitle()
                    }
                }
                item{
                    Spacer(modifier = Modifier.height(localSpacing.times(2)))
                }
                items(viewModel.users.size) { index ->
                    val item = viewModel.users[index]
                    AccountsItem(modifier= Modifier.fillMaxWidth(),account = item, eventListener = viewModel::onEvent)
                }
            }

            //buttom label
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Divider(modifier = Modifier.alpha(0.5f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(5f)
                            .fillMaxWidth(.5f)
                            .clickable {
                                navController.navigate(Screen.LoginScreen.route)
                            },
                    contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Switch Account",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(MaterialTheme.colors.surface)                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(5f)
                            .fillMaxWidth(.5f)
                            .clickable {
                                navController.navigate(Screen.SignupScreen.route)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Sign Up",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

        }
    }
}