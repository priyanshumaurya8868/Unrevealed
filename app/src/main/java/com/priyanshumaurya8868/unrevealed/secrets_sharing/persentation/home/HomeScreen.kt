package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_SECRET_ID
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.TextCard
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.Drawer
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.HomeScreenEvents
import com.priyanshumaurya8868.unrevealed.core.composable.CustomDialog
import com.priyanshumaurya8868.unrevealed.core.utils.Constants
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.NothingToShow
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.LogOutDialog
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.PostItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val toolbarHeight = 48.dp
val fabHeight = 72.dp //FabSize+Padding

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    screen_route :String? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val openDialog = remember{ mutableStateOf(false)}
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    LaunchedEffect(key1 = true) {
        screen_route?.let {
            navController.navigate(it)
        }

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is HomeViewModel.UiEvent.BackToWelcomeScreen -> {
                    navController.navigate(Screen.WelcomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
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


    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refreshFeeds() }) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            drawerContent = {
                Drawer(
                    state = state,
                    eventListener = viewModel::onEvents,
                    navController = navController,
                    openDialog = openDialog
                )
            },
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
                    .nestedScroll(nestedScrollConnection),
                contentAlignment = Alignment.Center
            ) {

                if (openDialog.value){
                    LogOutDialog(openDialog = openDialog) {
                        viewModel.onEvents(HomeScreenEvents.LogOutUser)
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(vertical = toolbarHeight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                ) {

                    //tog filters
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                horizontal = localSpacing,
                                vertical = localVerticalSpacing,
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            item {
                                val color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                                Card(
                                    shape = CircleShape.copy(CornerSize(100.dp)),
                                    border = BorderStroke(width = 1.dp, color = color),
                                    backgroundColor = MaterialTheme.colors.background,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 10.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            viewModel.onEvents(
                                                HomeScreenEvents.ChangeTag(
                                                    null
                                                )
                                            )
                                        }
                                ) {
                                    Text(
                                        "All",
                                        color = color,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }

                            items(state.tags.size) { index ->
                                val item = state.tags[index]
                                Row(modifier = Modifier.noRippleClickable {
                                    viewModel.onEvents(HomeScreenEvents.ChangeTag(item.name))
                                }) {
                                    if (state.selectedTag == item.name) {
                                        TextCard(
                                            text = item.name,
                                            backgroundColor = MaterialTheme.colors.primary,
                                            textColor = Color.White.copy(alpha = 0.8f)
                                        )
                                    } else {
                                        TextCard(text = item.name)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    //feed items
                    items(state.items.size) { index ->
                        val item = state.items[index]
                        if (index >= state.items.size - 1 && !state.endReached && !state.isPaginating) {
                            viewModel.loadNextItems()
                        }
                        Spacer(modifier = Modifier.height(localVerticalSpacing))
                        PostItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = localSpacing)
                                .clickable {
                                    navController.navigate(Screen.ViewSecretScreen.route + "?$ARG_SECRET_ID=${item._id}")
                                },
                            item = item,
                            navController = navController
                        )

                    }

                    if (state.items.isEmpty() && !(state.isPaginating || state.isRefreshing)){
                        item{
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                NothingToShow()
                            }
                        }
                    }

                    item {
                        if (state.isPaginating) {
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
                        .align(Alignment.TopStart)
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
}