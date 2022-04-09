package com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.viewPost.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components.NameWithTime
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.utils.DemoEntity

@Composable
fun CommentItem(dullColor: Color, highLighted: Color, inbtwnColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(localSpacing)
    ) {
        CircleImage(image = painterResource(id = R.drawable.female_4), size = 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NameWithTime(username = "SomeName", timeString = "just now")

                CommentsFeature(dullColor = dullColor, highLighted = highLighted)
            }
            Text(
                DemoEntity().comment,
                fontWeight = FontWeight.W400,
                color = inbtwnColor,
                modifier = Modifier.padding(vertical = localVerticalSpacing)
            )


        }

    }

}


@Composable
fun CommentsFeature(dullColor: Color, highLighted: Color, entity: DemoEntity = DemoEntity()) {
    Row() {
        //TODO : if Liked  favourite else favourite border
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Favourite",
            modifier = Modifier.size(20.dp),
            tint = dullColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        //Like Count
        Text(
            text = entity.likeCount.toString(),
            style = MaterialTheme.typography.caption,
            color = dullColor
        )

        Spacer(modifier = Modifier.width(localSpacing))


        //TODO: only  visible with commenter itself visiting it
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "option",
            modifier = Modifier.size(20.dp),
            tint = dullColor
        )
    }
}
