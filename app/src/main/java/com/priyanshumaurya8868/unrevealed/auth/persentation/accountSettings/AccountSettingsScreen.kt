package com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.ChangeAvatar
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.GetAvatars
import com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings.composable.AvatarDialog
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog
import com.priyanshumaurya8868.unrevealed.core.composable.UserBriefDetail
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes.BASE_URL
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.toolbarHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountSettingsScreen(
    navController: NavController,
    viewModel: AccountSettingsViewModel,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    val TAG = "AccountSettingsScreen"

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val myProfile = state.profile
    val openDialogDA = remember {
        mutableStateOf(false)
    }
    val openDialogCA = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        //fetching avatar list in advance
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AccountSettingsViewModel.UiEvents.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.msg)
                }
               is AccountSettingsViewModel.UiEvents.ProceedToWelScreen->{
                   Log.d(TAG, "fired evnt recieve")
                   navController.navigate(Screen.WelcomeScreen.route){
                       popUpTo(Screen.WelcomeScreen.route){
                           inclusive = true
                       }
                   }
               }
            }
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(
                        modifier = Modifier.height(
                            localSpacing.times(2).plus(toolbarHeight)
                        )
                    )
                }
                item { CircleImage(image = myProfile.avatar, size = 120.dp, modifier = Modifier.clip(
                    CircleShape).clickable { openDialogCA.value = true }) }
                item {
                    UserBriefDetail(
                        modifier = Modifier.padding(localVerticalSpacing).clickable { openDialogCA.value = true },
                        username = myProfile.username,
                        gender = myProfile.gender,
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth().noRippleClickable { openDialogCA.value = true },
                        text = "Change Avatar",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = fontSize_1
                    )
                }
                item { Spacer(modifier = Modifier.height(localSpacing.times(2))) }
                item {
                    SettingsOptions(text = "Change Password") { scope.launch { modalBottomSheetState.show() }}
                }
                item {
                    SettingsOptions(text = "Deactivate Account") { openDialogDA.value =true }
                }
            }

            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight).align(Alignment.TopStart),
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                title = {
                    Text("Account Settings")
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        null,
                        modifier = Modifier
                            .padding(horizontal = localSpacing)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                },
            )
            if (openDialogDA.value) {
                CustomDialog(
                    openDialog = openDialogDA,
                    onActionClickListener = { viewModel.onEvent(AccountSettingsEvent.DeactivateAccount) },
                    title = "Delete Account?",
                    description = "Do You Really Want To Delete your account with your all data, it'll be removed permanently.",
                    actionString = "Yes, I'm",
                    dismissalString = "Cancel"
                )
            }
            if (viewModel.state.isLoading) CircularProgressIndicator()
            if (openDialogCA.value ){
                viewModel.getAvatars()
                AvatarDialog(
                    onSelectionListener = {
                     viewModel.onEvent(AccountSettingsEvent.UpdateAvatar(it.removePrefix(BASE_URL)))
                        openDialogCA.value = false
                    },
                    avatars = state.listOfAvatars,
                    isLoading = state.isLoading,
                    errString = state.errorMsg
                )
            }
        }
    }
}

@Composable
fun SettingsOptions(text: String, onclick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick() }, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Divider()
        Text(
            text, modifier = Modifier
                .fillMaxWidth()
                .padding(localSpacing), color = MaterialTheme.colors.primary
        )
    }
}
