package com.mib.sage.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mib.sage.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Transaction admin",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // To ensures that your notifications are categorized and displayed properly on the user's device.
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Extract data fields and show notification
            val title = remoteMessage.data["title"]
            val description = remoteMessage.data["description"]
            showNotification(title, description)
        } else {
            // Check if message contains notification payload
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")

                // Extract data fields from notification payload
                val title = it.title
                val description = it.body
                showNotification(title, description)
            }
        }
    }

    private fun showNotification(title: String?, desc: String?) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Display the notification
        val manager = NotificationManagerCompat.from(this)
        manager.notify(ID_TRANSACTION_NOTIFICATION, builder.build())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "sageadmin123"
        private const val ID_TRANSACTION_NOTIFICATION = 3032024
    }
}
