package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@Composable
fun Profiler(
    modifier: Modifier = Modifier,
    image: Painter,
    imageSize : Dp= 100.dp,
    username: String,
    gender: String
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

       CircleImage(image = image, size = imageSize)
        Spacer(modifier = Modifier.height(localSpacing))
        Text(text = username, fontSize = fontSize_1)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = gender, fontWeight = FontWeight.Light, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                imageVector =
                if (gender == VAL_MALE)
                    Icons.Default.Male
                else Icons.Default.Female,
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
                    .alpha(.7f)
            )
        }
    }
}


@Composable
fun CircleImage(image :Painter, size : Dp){
    Card(
        backgroundColor =
//            if (selectedIndex == it)
        MaterialTheme.colors.secondary
//            else MaterialTheme.colors.surface
        ,
        shape = CircleShape,
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
    ) {
        Image(
            painter = image,
            contentDescription = "Profile picture",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .padding(top = 10.dp, start = 5.dp, end = 5.dp)
        )
    }
}