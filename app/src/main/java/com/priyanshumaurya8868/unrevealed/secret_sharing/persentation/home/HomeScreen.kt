package com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components.Drawer
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components.PostItem
import com.priyanshumaurya8868.unrevealed.utils.Screen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val toolbarHeight = 48.dp
val fabHeight = 72.dp //FabSize+Padding

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {


    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }


    val fabHeightPx = with(LocalDensity.current) { fabHeight.roundToPx().toFloat() }
    val fabOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                val topAppBarNewOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = topAppBarNewOffset.coerceIn(-toolbarHeightPx, 0f)

                val fabNewOffset = fabOffsetHeightPx.value + delta
                fabOffsetHeightPx.value = fabNewOffset.coerceIn(-fabHeightPx, 0f)

                return Offset.Zero
            }
        }
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerContent = { Drawer() },
        drawerBackgroundColor = Color.Transparent,
        drawerElevation = 0.dp,
        drawerScrimColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.value.roundToInt()) },
                onClick = {}
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
                items(100) { index ->
                    Spacer(modifier = Modifier.height(localVerticalSpacing))
                    PostItem(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = localSpacing)
                        .clickable { navController.navigate(Screen.ViewPostScreen.route) })

                }
            }

            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                backgroundColor = MaterialTheme.colors.background.copy(0.8f),
                contentColor = MaterialTheme.colors.onSurface.copy(.5f),
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