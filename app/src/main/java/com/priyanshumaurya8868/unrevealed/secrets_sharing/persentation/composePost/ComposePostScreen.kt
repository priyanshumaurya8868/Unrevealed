package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.ComposePostScreenEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.TextCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposePostScreen(
    navController: NavController,
    modalBottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    viewModel: ComposePostViewModel
) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ComposePostViewModel.UiEvents.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.msg)
                }
                is ComposePostViewModel.UiEvents.Proceed -> {
                    val feedItemJsonStr = Json.encodeToString(event.feedItem)
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

            BackHandler {
                if(viewModel.state.content.isNotBlank())
                openDialog.value = true
                else navController.popBackStack()
            }

            val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

            if (openDialog.value){
                CustomDialog(
                    openDialog = openDialog,
                    onActionClickListener = { navController.popBackStack() ;},
                    title = "Are You Sure?",
                    description = "Do You Really Want To Discard it, it'll be removed permanently.",
                    actionString = "Yes, I'm",
                    dismissalString = "Cancel"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(localSpacing)
            ) {
                //Toolbar...
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back button",
                            modifier = Modifier.clickable { dispatcher.onBackPressed() })
                        Spacer(modifier = Modifier.width(25.dp))
                        Text(text = "Write a Secret", style = MaterialTheme.typography.h5)
                    }

                    if (state.content.isNotBlank()) {
                        if (state.isUploading) {
                            CircularProgressIndicator(
                                strokeWidth = 1.dp,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Text(
                                text = "Reveal",
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(horizontal = 15.dp)
                                    .clickable {
                                        viewModel.onEvent(ComposePostScreenEvents.Reveal) //TODO SAVE
                                    }
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = localSpacing),
                ) {

                    TextCard(text = "English")
                    Spacer(Modifier.width(localSpacing))
                    TextCard(
                        state.tag,
                        modifier = Modifier.clickable { scope.launch { modalBottomSheetState.show() } }
                    )
                }

                TextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    value = state.content,
                    onValueChange = { newText ->
                        viewModel.onEvent(ComposePostScreenEvents.OnContentChange(newText))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onBackground,
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    placeholder = { Text("What's on your mind?", fontSize = 20.sp) },
                    textStyle = TextStyle(fontSize = 20.sp),
                )
            }
        }
    }

}