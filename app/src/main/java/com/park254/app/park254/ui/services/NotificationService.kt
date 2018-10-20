
package com.park254.app.park254.ui.services

import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val notification = NotificationCompat.Builder(this, "Notification")
                .setContentTitle(remoteMessage?.data?.get("title"))
        .setContentText(remoteMessage?.data?.get("body"))
        //.setSmallIcon(R.mipmap.ic_launcher)
                .build();
        val manager = NotificationManagerCompat.from(applicationContext);
        manager.notify(123, notification)
    }
}