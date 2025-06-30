package com.example.recipelist.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationManager {
    fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Gerenciador de notificações"
            val descriptionText = "Canal para gerenciar as notificações do app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notification_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    fun sendNotification(context: Context, title: String, message: String){
        val builder = NotificationCompat.Builder(context, "notification_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)){
            notify(title.hashCode(), builder.build())
        }
    }
}