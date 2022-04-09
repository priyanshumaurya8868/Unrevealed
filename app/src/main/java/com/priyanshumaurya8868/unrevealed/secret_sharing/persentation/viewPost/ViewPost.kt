package com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.viewPost

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components.PostItem
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.utils.DemoEntity
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.viewPost.components.CommentItem
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.viewPost.components.CommentTextField


val topheadingSize = 24.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPost(navController: NavController) {

    val dullColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    val highLighted = MaterialTheme.colors.onSurface
    val inbtwnColor = MaterialTheme.colors.onSurface.copy(alpha = .75f)

    val post = DemoEntity()

    var comntStr by remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
    ) {


        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
            ) {
                item {
                    PostItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = Int.MAX_VALUE,
                        shape = RoundedCornerShape(bottomStart = 30.dp)
                    )
                }

                item {
                    BottomLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(localSpacing),
                        post = post,
                        dullColor = dullColor,
                        highLighted = highLighted
                    )
                }
                items(100) {
                    CommentItem(
                        dullColor = dullColor,
                        highLighted = highLighted,
                        inbtwnColor = inbtwnColor
                    )
                }
            }

            //TextField for comments
            CommentTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colors.background),
                comntStr = comntStr,
                dullColor = dullColor,
                highLighted = highLighted,
                onValueChange = { comntStr = it }
            ) {
                //TODO : action
            }
        }

    }

}


@Composable
fun BottomLabel(
    modifier: Modifier = Modifier,
    post: DemoEntity,
    dullColor: Color,
    highLighted: Color
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = buildAnnotatedString {
                when (post.commentCount) {
                    0 -> append("No Comments")
                    1 -> append("Comment")
                    else -> append("${post.commentCount} Comments")
                }
            },
            color = dullColor,
            fontSize = topheadingSize
        )

        Box(
            modifier = Modifier.border(
                width = 1.dp,
                shape = RoundedCornerShape(30.dp),
                color = dullColor
            )
        ) {
            Text(
                text = "Download Image",
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                color = highLighted
            )
        }
    }
}
