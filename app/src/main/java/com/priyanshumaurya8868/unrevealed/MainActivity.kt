package com.priyanshumaurya8868.unrevealed

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings.AccountSettingsScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings.ChangePasswordBottomSheet
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.AuthOptionsScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.AvatarSelection
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.GenderSelectionScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.LoginScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.SignupScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.WelcomeScreen
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.ThemeSwitcher
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_GENDER
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_PASSWORD
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_SECRET_ID
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_SECRET_ITEM
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USER
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USERNAME
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.DarkColorPalette
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.KEY_ROUTE
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.LightColorPalette
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.TagSelectionBottomSheet
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.HomeScreen
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.profileScreen.ProfileScreen
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    lateinit var themeSwitcher: ThemeSwitcher

    val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("omegaRanger", "Got Route in ma ${intent.getStringExtra(KEY_ROUTE)}")
        viewModel.setDirectNavRoute(intent.getStringExtra(KEY_ROUTE))
        FirebaseApp.initializeApp(this)
        setTheme(R.style.Theme_Unrevealed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.comments_notification_channel_id)
            val channelName = getString(R.string.comments_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]

        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic("comment")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg)
            }
        // [END subscribe_topics]


        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
        })
        // [END log_reg_token]


        setContent {
            ProvideWindowInsets {
                MaterialTheme(colors = if (themeSwitcher.IS_DARK_THEME) DarkColorPalette else LightColorPalette) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {

                        val shouldAuth: Boolean = viewModel.shouldAuthenticated
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            //TODO : splash screen with animation (quote  fetch from local db )
                            startDestination =
                            if (shouldAuth) Screen.WelcomeScreen.route else Screen.HomeScreen.route
                        ) {

                            composable(Screen.WelcomeScreen.route) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                WelcomeScreen(navController = navController)
                            }

                            composable(Screen.AuthOptionsScreen.route) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                AuthOptionsScreen(navController = navController)
                            }

                            composable(Screen.SignupScreen.route) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                SignupScreen(navController = navController)
                            }
                            composable(Screen.LoginScreen.route) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                LoginScreen(navController = navController)
                            }

                            composable(
                                Screen.GenderSelectionScreen.route + "?$ARG_USERNAME={$ARG_USERNAME}&$ARG_PASSWORD={$ARG_PASSWORD}",
                                arguments = listOf(
                                    navArgument(
                                        name = ARG_USERNAME
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_PASSWORD
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                GenderSelectionScreen(navController = navController)
                            }

                            composable(route =
                            Screen.AvatarSelectionScreen.route +
                                    "?$ARG_USERNAME={$ARG_USERNAME}&$ARG_PASSWORD={$ARG_PASSWORD}&$ARG_GENDER={$ARG_GENDER}",
                                arguments = listOf(
                                    navArgument(
                                        name = ARG_USERNAME
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_PASSWORD
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_GENDER
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                AvatarSelection(navController = navController)
                            }
                            composable(Screen.HomeScreen.route) {
                                HomeScreen(
                                    navController = navController,
                                    screen_route = viewModel.getDirectNavRoute()
                                )
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                            }
                            composable(Screen.ComposePostScreen.route + "?$ARG_SECRET_ITEM={$ARG_SECRET_ITEM}",
                                arguments = listOf(
                                    navArgument(ARG_SECRET_ITEM) {
                                        nullable = true
                                        type = NavType.StringType
                                        defaultValue = null
                                    }
                                )) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                TagSelectionBottomSheet(navController)
                            }
                            composable(Screen.ProfileScreen.route + "?$ARG_USER={$ARG_USER}",
                                arguments = listOf(
                                    navArgument(ARG_USER) {
                                        nullable = true
                                        type = NavType.StringType
                                        defaultValue = null
                                    }
                                )) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                ProfileScreen(navController)
                            }

                            composable(
                                Screen.ViewSecretScreen.route + "?$ARG_SECRET_ID={$ARG_SECRET_ID}",
                                arguments = listOf(
                                    navArgument(ARG_SECRET_ID) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.surface
                                )
                                ViewSecretScreen(navController = navController)
                            }
                            composable(Screen.AccountsSettings.route) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                                ChangePasswordBottomSheet(navController)
                            }
                        }
                    }
                }
            }
        }
    }

    data class NavRoute(val route : String? = null,val isVisited : Boolean = false)

    companion object {

        private const val TAG = "MainActivity"
    }
}
