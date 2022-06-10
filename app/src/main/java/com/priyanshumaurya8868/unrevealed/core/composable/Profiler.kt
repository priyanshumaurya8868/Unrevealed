package com.priyanshumaurya8868.unrevealed.core.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@Composable
fun Profiler(
    modifier: Modifier = Modifier,
    image: String,
    imageSize: Dp = 100.dp,
    username: String,
    gender: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CircleImage(image = image, size = imageSize)
        Spacer(modifier = Modifier.height(localSpacing))
        UserBriefDetail(username = username, gender = gender, textAlign = textAlign)
    }
}

@Composable
fun UserBriefDetail(
    modifier: Modifier = Modifier,
    username: String,
    gender: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Column(modifier = modifier, horizontalAlignment = if(textAlign == TextAlign.Center) Alignment.CenterHorizontally else Alignment.Start) {
        Text(
            text = username,
            fontSize = fontSize_1,
            color = MaterialTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (textAlign == TextAlign.Center) Arrangement.Center else Arrangement.Start
        ) {
            Text(
                text = gender,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface,
                textAlign = textAlign
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                imageVector =
                if (gender == VAL_MALE)
                    Icons.Default.Male
                else Icons.Default.Female,
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp),
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    image: String,
    size: Dp,
    contentDescription: String = "Profile picture"
) {
    Card(
        backgroundColor =
        MaterialTheme.colors.secondary,
        shape = CircleShape,
        modifier = modifier
            .size(size)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp)
        )
    }
}

