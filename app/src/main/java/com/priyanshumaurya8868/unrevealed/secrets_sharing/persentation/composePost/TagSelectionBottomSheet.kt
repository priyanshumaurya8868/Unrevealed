package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.ComposePostScreenEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.TextCard
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.NothingToShow
import kotlinx.coroutines.launch

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
        sheetState = modalBottomSheetState,sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = localSpacing)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = localVerticalSpacing),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Select a Topic!",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(vertical = localVerticalSpacing)
                    )
                    Icon(
                        Icons.Default.Close, "hide sheet",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = .7f),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { scope.launch { modalBottomSheetState.hide() } })
                }

                if (state.tags.isEmpty()) {
                    NothingToShow()
                } else
                    FlowRow(
                        modifier = Modifier.fillMaxSize(),
                        mainAxisSpacing = 10.dp,
                        crossAxisSpacing = 10.dp
                    ) {
                        state.tags.forEach { tag ->
                            val item = tag.name
                            TextCard(
                                text = item,
                                backgroundColor = if (state.tag == item) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                                textColor = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .clickable {
                                        viewModel.onEvent(
                                            ComposePostScreenEvents.ChooseTag(item)
                                        )
                                    }
                            )
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