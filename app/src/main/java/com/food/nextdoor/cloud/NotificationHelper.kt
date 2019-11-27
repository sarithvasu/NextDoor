package com.food.nextdoor.cloud

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import com.food.nextdoor.R


class NotificationHelper(context: Context): ContextWrapper(context) {

    val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan1 =  NotificationChannel(
                FIRST_CHANNEL,
                FIRST_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT)

        chan1.lightColor = Color.GREEN
        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(chan1)

        val chan2 = NotificationChannel(SECOND_CHANNEL, "Second channel", NotificationManager.IMPORTANCE_DEFAULT)
        chan2.lightColor = Color.GREEN
        chan2.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(chan2)

        val chan3 = NotificationChannel(THIRD_CHANNEL, "third channel", NotificationManager.IMPORTANCE_DEFAULT)
        chan3.lightColor = Color.GREEN
        chan3.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(chan3)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    fun getNfBuilderFromFirstChannel(title:String, body:String): Notification.Builder? {
        return   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(applicationContext, FIRST_CHANNEL)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.nonveg_symbol)
                .setAutoCancel(true)
        }
        else{
           return null
        }
    }

    fun getNfBuilderFromSecondChannel(title:String, body:String): Notification.Builder{
        return Notification.Builder(applicationContext,
            SECOND_CHANNEL
        )
            .setContentText(body)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.nonveg_symbol)
            .setAutoCancel(true)
    }

    fun getNfBuilderFromThiredChannel(title:String, body:String): Notification.Builder{
        return Notification.Builder(applicationContext,
            THIRD_CHANNEL
        )
            .setContentText(body)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.nonveg_symbol)
            .setAutoCancel(true)
    }

    fun Notify(id: Int, notification: Notification.Builder) {
        manager.notify(id, notification.build())
    }
    companion object {
        val FIRST_CHANNEL = "First Channel"
        val SECOND_CHANNEL = "Second Channel"
        val THIRD_CHANNEL = "Third Channel"
    }

}