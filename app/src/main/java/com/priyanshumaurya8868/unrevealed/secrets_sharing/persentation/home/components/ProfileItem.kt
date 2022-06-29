package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    textColor: Color,
    profile: MyProfile,
    isSelected: Boolean = false
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd){
                CircleImage(image = profile.avatar, size = 40.dp)
                if (isSelected) {
                    Icon(
                        painterResource(id = R.drawable.ic_check),
                        contentDescription = "selected Profile",
                        tint = Color.White,
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .border(1.dp, Color.White, shape = CircleShape)
                    )
                }
            }
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

    }

}


