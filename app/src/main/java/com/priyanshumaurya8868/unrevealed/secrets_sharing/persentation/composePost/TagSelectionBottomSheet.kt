package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.ComposePostScreenEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.TextCard

@OptIn(ExperimentalMaterialApi::class)

@Composable
@ExperimentalMaterialApi
fun TagSelectionBottomSheet(
    navController: NavController,
    viewModel: ComposePostViewModel = hiltViewModel()
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded
    )
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(localSpacing)
            ) {
                Text(
                    text = "Select a Topic",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(10.dp)
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                FlowRow(
                    modifier = Modifier.fillMaxSize(),
                    mainAxisSpacing = 10.dp,
                    crossAxisSpacing = 10.dp
                ) {
                    viewModel.tagList.forEach { item ->
                        Row(modifier = Modifier.clickable {
                            viewModel.onEvent(
                                ComposePostScreenEvents.ChooseTag(item)
                            )
                        }) {
                            if (state.tag == item) {
                                TextCard(
                                    text = item,
                                    backgroundColor = MaterialTheme.colors.primary,
                                    textColor = Color.White.copy(alpha = 0.8f)
                                )
                            } else {
                                TextCard(text = item)
                            }
                        }
                    }
                }
            }
        },
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.surface.copy(alpha = 0.55f),
    ) {
        ComposePostScreen(
            navController = navController,
            scope = scope,
            modalBottomSheetState = modalBottomSheetState,
            viewModel = viewModel
        )
    }

}