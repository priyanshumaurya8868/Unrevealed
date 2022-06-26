package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.topheadingSize

@Composable
fun BottomLabel(
    modifier: Modifier = Modifier,
    dullColor: Color,
    eventListener: (ViewSecretEvents) -> Unit,
    secret: FeedSecret,
    isEditor:Boolean
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Comments",
            color = dullColor,
            fontSize = topheadingSize
        )
//        Box(
//            modifier = Modifier.border(
//                width = 1.dp,
//                shape = RoundedCornerShape(30.dp),
//                color = dullColor
//            )
//        ) {
//            Text(
//                text = "Download Image",
//                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
//                color = highLighted
//            )
//        }
        if(isEditor){
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(localVerticalSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Icon(Icons.Default.EditNote, "edit",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                eventListener(ViewSecretEvents.UpdateSecret(secret))
                            }
                    )
                }
                item {
                    Icon(Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                eventListener(
                                    ViewSecretEvents.OpenDialog(
                                        ViewSecretViewModel.DialogMetaData(
                                            title = "Delete Secret?",
                                            description = "Are you sure do you want to delete your secret?. You won't able to undo this if you delete it once",
                                            confirmFun = {
                                                eventListener(
                                                    ViewSecretEvents.DeleteSecret(
                                                        secret._id
                                                    )
                                                )
                                            }
                                        )
                                    ))
                            }
                    )
                }
            }
        }

    }
}