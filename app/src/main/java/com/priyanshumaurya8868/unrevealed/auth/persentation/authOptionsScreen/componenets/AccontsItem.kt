package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage

@Composable
fun AccountsItem(
    modifier: Modifier = Modifier,
    account: Profile,
    eventListener: (AuthOptionScreenEvents) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleImage(image = rememberImagePainter(account.avatar), size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                account.username,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { eventListener(AuthOptionScreenEvents.LoginWith(account)) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                contentPadding = PaddingValues(2.dp)
            ) {
                Text("Login", color = MaterialTheme.colors.onBackground, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(localVerticalSpacing.div(2)))
            Icon(
                Icons.Default.Close,
                null,
                modifier = Modifier
                    .padding(5.dp)
                    .alpha(.5f)
                    .clickable { eventListener(AuthOptionScreenEvents.RemoveAccount(account)) })
        }

    }
}