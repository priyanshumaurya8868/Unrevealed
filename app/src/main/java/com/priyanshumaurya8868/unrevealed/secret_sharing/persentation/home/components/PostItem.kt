package com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components

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
import androidx.compose.ui.unit.sp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.utils.DemoEntity

@Composable
fun PostItem(
    modifier: Modifier,
    post: DemoEntity = DemoEntity(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    cardElevation: Dp = 1.dp,
    maxLines: Int = 4,
    shape: Shape = RoundedCornerShape(20.dp)
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
                    pfpPainter = painterResource(post.authorPfpId),
                    username = post.authorName,
                    timeString = post.time
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic__share),
                    contentDescription = "Share",
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = post.content,
                modifier = Modifier
                    .padding(vertical = 25.dp)
                    .fillMaxWidth(),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface.copy(0.8f),
            )

            PostMetaData(
                Modifier.fillMaxWidth(), post,
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                onBackgroundColor = MaterialTheme.colors.onSecondary.copy(alpha = .5f)
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
    dullColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.65f),
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
    post: DemoEntity,
    backgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    onBackgroundColor: Color = MaterialTheme.colors.onSecondary
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {

        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(30.dp),
            backgroundColor = backgroundColor
        ) {
            Body2(
                post.tag,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                color = onBackgroundColor
            )
        }

        Row {
            IconWithText(
                painterResource(id = R.drawable.ic_eye),
                post.viewCount,
                backgroundColor = backgroundColor,
                onBackgroundColor = onBackgroundColor
            )
            Spacer(modifier = Modifier.width(localSpacing))
            IconWithText(
                painterResource(id = R.drawable.ic_comments),
                post.commentCount,
                backgroundColor = backgroundColor,
                onBackgroundColor = onBackgroundColor
            )
        }

    }
}

@Composable
fun Body2(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = 12.sp,
    )

}

@Composable
fun IconWithText(
    painter: Painter,
    count: Int,
    backgroundColor: Color,
    onBackgroundColor: Color,
) = Row(verticalAlignment = Alignment.CenterVertically) {
    Card(shape = CircleShape, elevation = 0.dp, backgroundColor = backgroundColor) {
        Icon(
            painter = painter,
            contentDescription = "views count",
            modifier = Modifier
                .size(30.dp)
                .padding(6.dp),
            tint = onBackgroundColor
        )

    }
    Spacer(modifier = Modifier.width(10.dp))
    Body2(text = count.toString(), color = onBackgroundColor)
}
