package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    textColor: Color ,
    profile: MyProfile,
    isSelected : Boolean = false
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleImage(image = profile.avatar, size = 40.dp)
            Text(
                text = profile.username,
                fontSize = fontSize_1,
                color = textColor,
                modifier = Modifier.padding(
                    localVerticalSpacing
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if(isSelected){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(Color.Green.copy(alpha = .5f))
            ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = "selected Profile",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }


    }

}


