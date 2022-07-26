package com.priyanshumaurya8868.unrevealed.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.priyanshumaurya8868.unrevealed.MainActivity
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.AuthUseCases
import com.priyanshumaurya8868.unrevealed.core.utils.Constants
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.KEY_ROUTE
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

@AndroidEntryPoint
class MyFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var useCases : AuthUseCases
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            // Handle message within 10 seconds
            handleNow()
            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                sendNotification(it, remoteMessage.data,this)
                Log.d(TAG, "Message Notification payload: $it")
            }
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        scope.launch{
            useCases.savePreferences(PreferencesKeys.DEVICE_TOKEN, token)
            useCases.getLoggedUser().map {
                sendRegistrationToServer(token ,jwtToken = it.token)
            }
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String, jwtToken : String) = scope.launch {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")

        val inputData = Data.Builder()
            .putString(Constants.KEY_D_TOKEN,token)
            .putString(Constants.KEY_JWT_TOKEN,jwtToken)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(this@MyFirebaseService)
        workManager.enqueue(request)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @OptIn(ExperimentalFoundationApi::class)
    private fun sendNotification(
        messageBody: RemoteMessage.Notification,
        data: Map<String, String>,
        context: Context
    ) = scope.launch{
        var bitmap: Bitmap? = null
        val route = data.getOrDefault("screen_route", null)
        Log.d("omegaRanger", "GotRout To  display $route")
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(KEY_ROUTE,route)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        data.getOrDefault("dp", null)?.let{ avatar->
            Log.d("omegaRanger", "here is ur fpf : $avatar")

                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(HttpRoutes.BASE_URL+avatar)
                    .allowHardware(false) // Disable hardware bitmaps.
                    .build()

                val result = (loader.execute(request) as SuccessResult).drawable
                bitmap = (result as BitmapDrawable).bitmap
        }

        val channelId = getString(R.string.comments_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_letter_u)
            .setContentTitle(messageBody.title)
            .setContentText(messageBody.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setLargeIcon(bitmap)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_MAX
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }

//  calling  coroutine inside service
    private val job = SupervisorJob() //it is because i wanna  kill coroutine in onDestroy
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun foo() {
        scope.launch {
            // Call your suspend function
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}