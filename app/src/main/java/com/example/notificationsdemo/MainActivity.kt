package com.example.notificationsdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsdemo.ui.theme.NotificationsDemoTheme


class MainActivity : ComponentActivity() {
    var firstNotificationId = 0
    var lastNotificationId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            addNotification(this, lastNotificationId)
            lastNotificationId++
        }

        val buttonClear: Button = findViewById(R.id.buttonClear)
        buttonClear.setOnClickListener {
            cancelNotification(this, firstNotificationId)
            firstNotificationId++
        }

        createNotificationChannel(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                                   grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // can display a message here when permissions are granted/refused
    }

    fun addNotification(ctx: Context, id: Int) {
        val builder = NotificationCompat.Builder(this, "messageChannel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("You have a message: $id")
            .setContentText("Sample message text: $id")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

//        TODO set notification tap action
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0
                )
                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(id, builder.build())
        }
    }

    fun cancelNotification(ctx: Context, notifyId: Int) {
        val ns = Context.NOTIFICATION_SERVICE
        val nMgr = ctx.getSystemService(ns) as NotificationManager
        nMgr.cancel(notifyId)
    }
}

private fun createNotificationChannel(ctx: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    val name = "sample channel name"
    val descriptionText = "sample channel description"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val id = "messageChannel"
    val channel = NotificationChannel(id, name, importance).apply {
        description = descriptionText
    }
    // Register the channel with the system.
    val notificationManager: NotificationManager =
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

