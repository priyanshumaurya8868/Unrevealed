package com.priyanshumaurya8868.unrevealed.core.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    onDismissRequest: (() -> Unit)? = null,
    shouldShowCb: Boolean = false,
    cbLabelString: String = "",
    isChecked: Boolean = false,
    onCheckedChangeListener: (Boolean) -> Unit = {}
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
            onDismissRequest,
            shouldShowCb,
            cbLabelString,
            isChecked,
            onCheckedChangeListener
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
    onDismissRequest: (() -> Unit)? = null,
    shouldShowCb: Boolean,
    cbLabelString: String,
    isChecked: Boolean = false,
    onCheckedChangeListener: (Boolean) -> Unit
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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing.times(2)),
                    fontSize = 16.sp
                )
                if (shouldShowCb)
                    LabelledCheckbox(
                        labelString = cbLabelString,
                        isChecked = isChecked,
                        onCheckedChangeListener = { onCheckedChangeListener(it) }
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

@Composable
fun LabelledCheckbox(
    labelString: String,
    isChecked: Boolean,
    onCheckedChangeListener: (Boolean) -> Unit
) {
    Row(modifier = Modifier.padding(8.dp) , verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChangeListener(it) },
            enabled = true,
            colors = CheckboxDefaults.colors(MaterialTheme.colors.secondary)
        )
        Text(
            text = labelString,
            fontSize = 16.sp
        )
    }
}