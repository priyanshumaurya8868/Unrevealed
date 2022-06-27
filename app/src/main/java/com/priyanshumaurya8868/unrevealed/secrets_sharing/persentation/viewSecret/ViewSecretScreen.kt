package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.core.utils.Constants
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USER
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.NothingToShow
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.PostItem
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


val topheadingSize = 20.sp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewSecretScreen(
    navController: NavController,
    viewModel: ViewSecretViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val dullColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    val highLighted = MaterialTheme.colors.onSurface
    val state = viewModel.state
    val secret = state.secret

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ViewSecretViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is ViewSecretViewModel.UiEvent.MoveToComposePostScreen -> {
                    navController.navigate(Screen.ComposePostScreen.route + "?${Constants.ARG_SECRET_ITEM}=${event.secret}")
                }

                is ViewSecretViewModel.UiEvent.BackToHome -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        if (state.openDialog) {
            CustomDialog(
                onActionClickListener = { state.dialogMetaData!!.confirmFun() },
                title = state.dialogMetaData!!.title,
                description = state.dialogMetaData.description,
                actionString = "Ok",
                dismissalString = "Cancel",
                onDismissRequest = { viewModel.onEvent(ViewSecretEvents.CloseDialog) }
            )
        }

        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
                .fillMaxSize(),
        ) {

            if (state.isSecretLoading) { //TODO : Replace with shiver loading effect
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.secretErrorMsg != null && state.secret == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        state.secretErrorMsg,
                        style = MaterialTheme.typography.caption.copy(fontSize = 16.sp),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(
                            localSpacing
                        ),
                        textAlign = TextAlign.Center,
                    )
                    Button(onClick = { viewModel.onEvent(ViewSecretEvents.Retry) }) {
                        Text("Retry", style = TextStyle(fontSize = 18.sp))
                    }

                }
            } else {
                Box {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        contentPadding = PaddingValues(bottom = 170.dp)
                    ) {
                        item {
                            PostItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                maxLines = Int.MAX_VALUE,
                                shape = RoundedCornerShape(bottomStart = 30.dp),
                                item = secret ?: FeedSecret(),
                                shouldShowCommentCount = false,
                                navController = navController
                            )
                        }

                        item {
                            viewModel.state.secret?.let { secret ->
                                BottomLabel(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(localSpacing),
                                    dullColor = dullColor,
                                    eventListener = viewModel::onEvent,
                                    secret = secret,
                                    isEditor = viewModel.ownerId == secret.author._id
                                )
                            }
                        }

                        state.commentsState.forEachIndexed { commentPosition, _ ->
                            if (commentPosition >= state.commentsState.size - 1 && !state.endReached && !state.isCommentsLoading) {
                                viewModel.onEvent(ViewSecretEvents.LoadNextCommentPage)
                            }
                            val commentState = state.CmtMap.getOrDefault(
                                state.commentsState[commentPosition].comment._id,
                                state.commentsState[commentPosition]
                            )
                            val replies = commentState.replies
                            val comment = commentState.comment

                            item {
                                CommentItem(
                                    dullColor = dullColor,
                                    commentState = commentState,
                                    eventListener = viewModel::onEvent,
                                    commentPosition = commentPosition,
                                    ownerID = viewModel.ownerId ?: "",
                                ){
                                    navController.navigate(Screen.ProfileScreen.route+ "?$ARG_USER=${Json.encodeToString(commentState.comment.commenter)}")
                                }
                            }

                            val shouldToggleButtonVisible: Boolean =
                                replies.isNotEmpty() || comment.reply_count > 0
                            if (shouldToggleButtonVisible) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 8.dp, start = 80.dp)
                                            .noRippleClickable {
                                                viewModel.onEvent(
                                                    ViewSecretEvents.ChangeVisibilitiesOfReplies(
                                                        commentStateIndex = commentPosition,
                                                        commentState.comment._id
                                                    )
                                                )
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Divider(
                                            color = dullColor,
                                            modifier = Modifier
                                                .fillMaxWidth(.17f)
                                                .alpha(0.5f)
                                        )
                                        Spacer(modifier = Modifier.width(localVerticalSpacing))
                                        CommentMenuItems(
                                            text = if (commentState.isFetchingReplies) "Loading replies..." else buildAnnotatedString {
                                                if (commentState.areRepliesVisible)
                                                    append("Hide ${commentState.comment.reply_count} ")
                                                else
                                                    append("View ${commentState.comment.reply_count} ")
                                                if (commentState.comment.reply_count == 1)
                                                    append("reply")
                                                else append("replies")
                                            }.text,
                                            color = dullColor
                                        )
                                    }
                                }
                            }
                            else {
                                item{Spacer(Modifier.height(localSpacing))}
                            }

                            if (commentState.areRepliesVisible) {
                                items(commentState.replies.size) { replyIndex ->
                                    val reply = commentState.replies[replyIndex]
                                    ReplyItem(
                                        modifier = Modifier.padding(start = 100.dp, end= localSpacing, bottom = localSpacing),
                                        dullColor = dullColor,
                                        eventListener = { viewModel.onEvent(it) },
                                        commentPosition = commentPosition,
                                        replyPosition = replyIndex,
                                        reply = state.RpMap.getOrDefault(
                                            reply._id,
                                            reply
                                        ),
                                        isEditor = viewModel.ownerId == reply.commenter._id
                                    ){
                                        navController.navigate(Screen.ProfileScreen.route+ "?$ARG_USER=${Json.encodeToString(commentState.comment.commenter)}")
                                    }
                                }
                            }
                        }
//
                        if (state.commentsState.isEmpty() && !state.isCommentsLoading) {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    NothingToShow()
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                state.commentErrorMsg?.let { erroMsg ->
                                    Text(
                                        erroMsg,
                                        style = MaterialTheme.typography.caption.copy(fontSize = 16.sp),
                                        color = MaterialTheme.colors.onSurface,
                                        modifier = Modifier.padding(
                                            localSpacing
                                        ),
                                        textAlign = TextAlign.Center,

                                        )
                                    Button(onClick = { viewModel.loadNextItems() }) {
                                        Text("Retry", style = TextStyle(fontSize = 18.sp))
                                    }
                                }
                                if (state.isCommentsLoading) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }

                    }

                    //TextField for comments
                    CommentTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colors.background),
                        comntStr = state.textFieldState,
                        dullColor = dullColor,
                        highLighted = highLighted,
                        onValueChange = { viewModel.onEvent(ViewSecretEvents.OnWriting(it)) },
                        isPostingComment = state.isAlreadyPostingSomething,
                        replyMetaData = state.replyMetaData,
                        textColor = MaterialTheme.colors.onBackground,
                        updateComplimentMetaData = state.updateComplimentMetaData
                    ) {
                        viewModel.onEvent(it)
                    }
                }
            }

        }

    }

}



