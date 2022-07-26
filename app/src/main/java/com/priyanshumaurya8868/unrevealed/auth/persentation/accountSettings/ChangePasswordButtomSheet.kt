package com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.PasswordTextField
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.ResponsiveButton
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChangePasswordBottomSheet(
    navController: NavController,
    viewModel: AccountSettingsViewModel = hiltViewModel()
) {

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val state = viewModel.state
    val oldPassword = state.oldPassword.value
    val newPassword = state.newPassword.value
    val reEnteredNewPassword = state.reEnteredNewPassword.value
    var oldPasswordVisible by remember {
        mutableStateOf(false)
    }
    var newPasswordVisible by remember {
        mutableStateOf(false)
    }
    var reEnteredNewPasswordVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(localVerticalSpacing)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select a Topic",
                        style = MaterialTheme.typography.h5,
                    )
                    Icon(
                        Icons.Default.Close, "hide sheet",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = .7f),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { scope.launch { modalBottomSheetState.hide() } })
                }
                Spacer(modifier = Modifier.height(localSpacing))

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = oldPassword.text,
                    onValueChange = { viewModel.onEvent(AccountSettingsEvent.EnteringOldPassword(it)) },
                    isError = oldPassword.isError,
                    passwordVisible = oldPasswordVisible,
                    togglePassword = { oldPasswordVisible = !it },
                    hint = "Old Password"
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPassword.text,
                    onValueChange = { viewModel.onEvent(AccountSettingsEvent.EnteringNewPassword(it)) },
                    isError = newPassword.isError,
                    passwordVisible = newPasswordVisible,
                    togglePassword = { newPasswordVisible = !it },
                    hint = "New Password"
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reEnteredNewPassword.text,
                    onValueChange = { viewModel.onEvent(AccountSettingsEvent.ReEnteringPassword(it)) },
                    isError = reEnteredNewPassword.isError,
                    passwordVisible = reEnteredNewPasswordVisible,
                    togglePassword = { reEnteredNewPasswordVisible = !it },
                    hint = "ReEnter New Password"
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                ResponsiveButton(
                    content = { Text(text = "Change Password", modifier = Modifier.padding(8.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onEvent(AccountSettingsEvent.ChangePassword)
                        scope.launch { modalBottomSheetState.hide() }
                    },
                    enabled = oldPassword.text.isNotBlank() && newPassword.text.isNotBlank() && reEnteredNewPassword.text.isNotBlank()
                )
            }
        }
    ) {
        AccountSettingsScreen(
            navController = navController,
            scope = scope,
            modalBottomSheetState = modalBottomSheetState,
            viewModel = viewModel
        )
    }
}