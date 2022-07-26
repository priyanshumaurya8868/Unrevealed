package com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.AvatarGridView
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AvatarDialog(
    openDialog: MutableState<Boolean> = remember {
        mutableStateOf(true)
    },
    onSelectionListener: (String) -> Unit,
    avatars: List<String>,
    isLoading: Boolean,
    onDismissRequest: (() -> Unit)? = null,
    errString: String = ""
) {
    Dialog(onDismissRequest = {
        if (onDismissRequest != null) {
            onDismissRequest()
        } else openDialog.value = false
    }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) CircularProgressIndicator()
            if (errString.isNotBlank()) Text(
                text = errString,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = localSpacing.times(2)),
                fontSize = 16.sp
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Choose Avatar",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(localSpacing)
                )
                AvatarGridView(
                    list = avatars,
                    selectedImgCallback = onSelectionListener,
                )

            }
        }
    }
}