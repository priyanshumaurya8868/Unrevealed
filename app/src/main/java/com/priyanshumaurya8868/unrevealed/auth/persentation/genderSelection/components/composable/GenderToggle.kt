package com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.components.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
fun GenderToggle(
    modifier: Modifier = Modifier,
    size: Dp,
    leftComposable: @Composable (() -> Unit),
    rightComposable: @Composable (() -> Unit),
    isMaleCallback: (Boolean) -> Unit,
    isMale: Boolean = false
) {
    var _isMale by remember {
        mutableStateOf(isMale)
    }
    val offsetX by animateDpAsState(targetValue = if (_isMale) size.div(2) else 0.dp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            Modifier
                .width(size)
                .height(size.div(2))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
                {
                    _isMale = !_isMale
                    isMaleCallback(_isMale)
                },
            shape = RoundedCornerShape(size.div(2)),
            elevation = 0.dp
        ) {

            Box(
                modifier = Modifier
                    .width(size)
                    .height(size.div(2))
                    .padding(start = offsetX),
            ) {

                Card(
                    shape = CircleShape, modifier = Modifier
                        .size(size = size.div(2)),
                    backgroundColor = MaterialTheme.colors.secondary
                ) {}

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leftComposable()
                rightComposable()
            }

        }
        Spacer(modifier = Modifier.height(localVerticalSpacing))
        Row(
            modifier = Modifier.width(250.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Female")
            Text("Male")
        }
    }
}

