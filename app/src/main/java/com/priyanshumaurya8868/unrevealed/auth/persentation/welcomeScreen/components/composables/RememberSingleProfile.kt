package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@Composable
fun RememberSingleProfile(loginCallback: () -> Unit, removeCallback: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.my_pic),
                contentDescription = "Profile pic",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )//image

            Spacer(modifier = Modifier.height(localSpacing))
            Text(
                "Solitudinarian", fontSize = fontSize_1,
                color = MaterialTheme.colors.onSurface
            )//text
        }//column
        Spacer(modifier = Modifier.height(localSpacing))

        ChoiceMakingButtons(
            Modifier.fillMaxWidth(),
            btnText1 = "Login",
            btn1OnClick = loginCallback,
            btnText2 = "Remove",
            btn2OnClick = removeCallback
        )
    }
}

