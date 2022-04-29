package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.PostItem
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.BottomLabel
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.CommentItem
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.CommentTextField
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.ViewSecretEvents


val topheadingSize = 20.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewSecretScreen(
    navController: NavController,
    viewmodel: ViewSecretViewModel = hiltViewModel()
) {

    val dullColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    val highLighted = MaterialTheme.colors.onSurface
    val inbtwnColor = MaterialTheme.colors.onSurface.copy(alpha = .75f)

    val state = viewmodel.state
    val secret = state.secret


    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
    ) {

        if (state.isSecretLoading) { //TODO : Replace with shiver loading effect
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.secretErrorMsg != null && state.secret != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    state.secretErrorMsg,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(
                        localSpacing
                    )
                )
                Button(onClick = { viewmodel.onEvent(ViewSecretEvents.ReloadSecret) }) {
                    Text("Retry")
                }

            }
        } else {
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = localSpacing.times(2)),
                    verticalArrangement = Arrangement.Top,

                    ) {
                    item {
                        PostItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = Int.MAX_VALUE,
                            shape = RoundedCornerShape(bottomStart = 30.dp),
                            item = secret ?: FeedSecret(),
                            shouldShowCommentCount = false
                        )
                    }

                    item {
                        BottomLabel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(localSpacing),
                            dullColor = dullColor,
                            highLighted = highLighted
                        )
                    }
                    items(state.comments.size) { index ->
                        if (index >= state.comments.size - 1 && !state.endReached && !state.isCommentsLoading) {
                            viewmodel.onEvent(ViewSecretEvents.LoadNextCommentPage)
                        }
                        CommentItem(
                            dullColor = dullColor,
                            highLighted = highLighted,
                            inbtwnColor = inbtwnColor,
                            comment = state.map.getOrDefault(
                                state.comments[index]._id,
                                state.comments[index]
                            ),
                            reactionListener = viewmodel::onEvent,
                            index
                        )
                    }
                    item {
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

                //TextField for comments
                CommentTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colors.background),
                    comntStr = state.textFieldState,
                    dullColor = dullColor,
                    highLighted = highLighted,
                    onValueChange = { viewmodel.onEvent(ViewSecretEvents.OnWritingComment(it)) },
                    isPostingComment = state.isPostingComment,
                ) {
                    viewmodel.onEvent(ViewSecretEvents.PostComment)
                }
            }
        }

    }

}



