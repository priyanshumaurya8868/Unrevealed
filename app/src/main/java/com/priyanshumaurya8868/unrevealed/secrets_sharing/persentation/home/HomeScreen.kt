package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.Drawer
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.PostItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val toolbarHeight = 48.dp
val fabHeight = 72.dp //FabSize+Padding

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }


    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }

    val fabHeightPx = with(LocalDensity.current) { fabHeight.roundToPx().toFloat() }
    var fabOffsetHeightPx by remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                val topAppBarNewOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = topAppBarNewOffset.coerceIn(-toolbarHeightPx, 0f)

                val fabNewOffset = fabOffsetHeightPx + delta
                fabOffsetHeightPx = fabNewOffset.coerceIn(-fabHeightPx, 0f)

                return Offset.Zero
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(user = state.myProfile) },
        drawerBackgroundColor = Color.Transparent,
        drawerElevation = 0.dp,
        drawerScrimColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.roundToInt()) },
                onClick = {
                    navController.navigate(Screen.ComposePostScreen.route)
                }
            ) {
                Icon(Icons.Filled.Add, "Post new Secrets", tint = Color.White)
            }
        }
    ) {


        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {

            LazyColumn(
                contentPadding = PaddingValues(vertical = toolbarHeight),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(state.items.size) { index ->
                    val item = state.items[index]
                    if (index >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                        viewModel.loadNextItems()
                    }
                    Spacer(modifier = Modifier.height(localVerticalSpacing))
                    PostItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = localSpacing)
                            .clickable {
//                                navController.navigate(Screen.ViewPostScreen.route + "?$ARG_SECRET_ID=${item._id}")
                                       },
                       item = item
                    )

                }
                item {
                    if (state.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text("Secrets") },
                navigationIcon = {
                    Icon(
                        Icons.Default.Menu,
                        null,
                        modifier = Modifier
                            .padding(horizontal = localSpacing)
                            .clickable {
                                scope.launch { scaffoldState.drawerState.open() }
                            }
                    )
                },
            )


        }
    }
}