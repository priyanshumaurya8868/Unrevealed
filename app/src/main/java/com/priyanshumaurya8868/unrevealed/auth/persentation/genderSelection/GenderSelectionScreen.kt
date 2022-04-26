package com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.components.composable.GenderToggle
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.components.composable.TitleWithDescription
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.fontSize_1
import com.priyanshumaurya8868.unrevealed.core.Constants
import com.priyanshumaurya8868.unrevealed.core.Screen


val titleSize: TextUnit = 24.sp

@Composable
fun GenderSelectionScreen(
    navController: NavController,
    viewModel: GenderSelectionViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        TitleWithDescription(
            modifier = Modifier
                .fillMaxWidth(),
            title = "Choose your gender",
            description = "This will help us in the selection of your avatar.",
            titleSize = titleSize
        )
        GenderToggle(
            size = 250.dp,
            modifier = Modifier.fillMaxWidth(),
            leftComposable = {
                Icon(
                    imageVector = Icons.Default.Female,
                    contentDescription = "Female",
                    modifier = Modifier.size(50.dp),
                    tint = if (viewModel.isMale.value) MaterialTheme.colors.onSurface else Color.White
                )
            },
            rightComposable = {
                Icon(
                    imageVector = Icons.Default.Male,
                    contentDescription = "Male",
                    modifier = Modifier.size(50.dp),
                    tint = if (!viewModel.isMale.value) MaterialTheme.colors.onSurface else Color.White
                )
            },
            isMaleCallback = viewModel::onGenderChange,
            isMale = viewModel.isMale.value
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                shape = RoundedCornerShape(50.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = "Back",
                    fontSize = fontSize_1,
                    modifier = Modifier.padding(horizontal = 60.dp, vertical = 25.dp),
                    color = MaterialTheme.colors.onSurface
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                shape = RoundedCornerShape(50.dp),
                onClick = {
                    navController.navigate(
                        Screen.AvatarSelectionScreen.route +
                                "?${Constants.ARG_USERNAME}=${viewModel.username}" +
                                "&${Constants.ARG_PASSWORD}=${viewModel.password}" +
                                "&${Constants.ARG_GENDER}=${viewModel.gender}"
                    )
                }
            ) {

                Text(
                    text = "Next",
                    fontSize = fontSize_1,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 60.dp, vertical = 25.dp)
                )
            }

        }
    }

}







