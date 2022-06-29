package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog

@Composable
fun LogOutDialog(
    openDialog: MutableState<Boolean>,
    onActionClickListener: () -> Unit,
    isChecked: Boolean,
    onCheckedChangeListener: (Boolean) -> Unit,
    ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CustomDialog(
            openDialog = openDialog,
            onActionClickListener = onActionClickListener,
            title = "Log Out?",
            description = "You can always access your account by signing back in",
            actionString = "Log Out",
            dismissalString = "Cancel",
            shouldShowCb = true,
            isChecked = isChecked,
            cbLabelString = "Keep my credentials.",
            onCheckedChangeListener = onCheckedChangeListener
        )

    }
}

