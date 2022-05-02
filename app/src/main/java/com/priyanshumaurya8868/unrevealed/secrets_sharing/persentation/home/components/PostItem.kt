package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.covertToPostTimeText
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret

@Composable
fun PostItem(
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    cardElevation: Dp = 1.dp,
    maxLines: Int = 3,
    shape: Shape = RoundedCornerShape(20.dp),
    item: FeedSecret,
    shouldShowCommentCount: Boolean = true
) {

    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        elevation = cardElevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(localSpacing)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AuthorProfiler(
                    Modifier.fillMaxWidth(),
                    pfpPainter = rememberImagePainter(item.author.avatar),
                    username = item.author.username,
                    timeString = item.timestamp.covertToPostTimeText() ?: ""
                )

//                Icon(
//                    Icons.Default.Share,
//                    contentDescription = "Share",
//                    modifier = Modifier.size(14.dp)
//                )
            }

            Text(
                text = item.content,
                modifier = Modifier
                    .padding(vertical = 25.dp)
                    .fillMaxWidth(),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onBackground,
            )

            PostMetaData(
                Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background.copy(alpha = .2f),
                onBackgroundColor = MaterialTheme.colors.onSurface,
                tag = item.tag,
                view_count = item.views_count,
                comments_count = item.comments_count,
                shouldShowCommentCount = shouldShowCommentCount
            )

        }
    }
}


@Composable
fun AuthorProfiler(
    modifier: Modifier = Modifier,
    pfpPainter: Painter,
    username: String,
    timeString: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            CircleImage(image = pfpPainter, size = 50.dp)

            Spacer(modifier = Modifier.width(localVerticalSpacing))
            NameWithTime(username, timeString)
        }


    }
}

@Composable
fun NameWithTime(
    username: String, timeString: String,
    dullColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
    highlightedColor: Color = MaterialTheme.colors.onBackground
) {
    Column {
        Text(text = username, fontWeight = FontWeight.W400, color = highlightedColor) //sub-title
        Text(
            text = timeString,
            color = dullColor,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
fun PostMetaData(
    modifier: Modifier,
    tag: String,
    view_count: Int,
    comments_count: Int,
    backgroundColor: Color = MaterialTheme.colors.background,
    onBackgroundColor: Color = MaterialTheme.colors.onSurface,
    shouldShowCommentCount: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {

        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(30.dp),
            backgroundColor = backgroundColor,
//            modifier = Modifier.border(
//                1.dp.div(4), onBackgroundColor,
//                RoundedCornerShape(30.dp)
//            )
        ) {
            Text(
                tag,
                style = MaterialTheme.typography.body2,
                color = onBackgroundColor,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
            )
        }

        Row {
            IconWithText(
                painterResource(id = R.drawable.ic_eye),
                view_count,
                backgroundColor = backgroundColor,
                onBackgroundColor = onBackgroundColor
            )
            if (shouldShowCommentCount) {
                Spacer(modifier = Modifier.width(localSpacing))
                IconWithText(
                    painterResource(id = R.drawable.ic_comments),
                    comments_count,
                    backgroundColor = backgroundColor,
                    onBackgroundColor = onBackgroundColor
                )
            }
        }

    }
}


@Composable
fun IconWithText(
    painter: Painter,
    count: Int,
    backgroundColor: Color,
    onBackgroundColor: Color,
) = Row(verticalAlignment = Alignment.CenterVertically) {
    Card(
        shape = CircleShape,
        elevation = 0.dp,
        backgroundColor = backgroundColor
    ) {
        Icon(
            painter = painter,
            contentDescription = "views count",
            modifier = Modifier
                .size(30.dp)
                .padding(6.dp),
            tint = onBackgroundColor.copy(0.8f)
        )

    }
    Spacer(modifier = Modifier.width(10.dp))
    Text(
        text = count.toString(),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface
    )
}
