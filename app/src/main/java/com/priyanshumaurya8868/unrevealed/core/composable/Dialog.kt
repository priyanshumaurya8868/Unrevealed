package com.priyanshumaurya8868.unrevealed.core.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@Composable
fun CustomDialog(
    openDialog: MutableState<Boolean> = remember { mutableStateOf(true) },
    onActionClickListener: () -> Unit,
    title: String,
    description: String,
    actionString: String,
    dismissalString: String,
    onDismissRequest: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = {
        if (onDismissRequest != null) {
            onDismissRequest()
        } else openDialog.value = false
    }) {
        DialogLayout(
            onActionClickListener,
            openDialog,
            title,
            description,
            actionString,
            dismissalString,
            onDismissRequest
        )
    }
}

@Composable
fun DialogLayout(
    onClick: () -> Unit,
    openDialog: MutableState<Boolean>,
    title: String,
    description: String,
    actionString: String,
    dismissalString: String,
    onDismissRequest: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //textual stuff
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(localSpacing)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing.times(2))
                        .padding(bottom = localSpacing)
                )
            }
            //action buttons
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.3f)
                )
                Text(text = actionString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                        .padding(localSpacing),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.3f)
                )
                Text(text = dismissalString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (onDismissRequest != null) {
                                onDismissRequest()
                            } else openDialog.value = false
                        }
                        .padding(localSpacing),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}