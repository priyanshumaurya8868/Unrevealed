package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.profileScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.Profiler
import com.priyanshumaurya8868.unrevealed.core.utils.Constants
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.PostItem

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(localSpacing),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Profiler(
                        modifier = Modifier.fillMaxWidth(),
                        image = state.userProfile.avatar,
                        username = state.userProfile.username,
                        gender = state.userProfile.gender,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Secrets",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                    )
                }
            }

            items(state.secrets.size) { index ->
                val item = state.secrets[index]
                if (index >= state.secrets.size - 1 && !state.endReached && !state.isLoading) {
                    viewModel.loadNextItems()
                }
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                PostItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing)
                        .clickable {
                            navController.navigate(Screen.ViewSecretScreen.route + "?${Constants.ARG_SECRET_ID}=${item._id}")
                        },
                    item = item,
                    navController = navController
                )
            }

            item {
                Row(modifier = Modifier.padding(localSpacing)) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }
}

