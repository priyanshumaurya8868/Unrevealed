package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog

@Composable
fun LogOutDialog(openDialog: MutableState<Boolean>, onActionClickListener: ()->Unit) {
    CustomDialog(
        openDialog = openDialog,
        onActionClickListener =onActionClickListener,
        title = "Log Out?",
        description = "You can always access your account by signing back in",
        actionString = "Log Out",
        dismissalString = "Cancel"
    )
}