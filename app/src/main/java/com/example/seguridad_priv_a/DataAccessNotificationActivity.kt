package com.example.seguridad_priv_a

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.seguridad_priv_a.databinding.ActivityDataAccessNotificationBinding

class DataAccessNotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataAccessNotificationBinding

    private val CHANNEL_ID = "data_access_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAccessNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.btnEnableNotifications.setOnClickListener {
            showDataAccessNotification("Acceso a la cámara detectado")
        }
    }

    // Crear un canal de notificaciones para dispositivos con Android O o superior
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Data Access Notifications"
            val descriptionText = "Notifications for sensitive data access"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Mostrar una notificación de acceso a datos
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showDataAccessNotification(message: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_data_access_notification) // Usar el icono de notificación
            .setContentTitle("Acceso a Datos Sensibles")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
        Toast.makeText(this, "Notificación enviada: $message", Toast.LENGTH_SHORT).show()
    }
}
