package com.example.seguridad_priv_a

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seguridad_priv_a.adapter.PermissionsAdapter
import com.example.seguridad_priv_a.data.PermissionItem
import com.example.seguridad_priv_a.data.PermissionStatus
import com.example.seguridad_priv_a.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionsAdapter: PermissionsAdapter
    private val dataProtectionManager by lazy {
        (application as PermissionsApplication).dataProtectionManager
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissions = listOf(
        PermissionItem(
            name = "Cámara",
            description = "Tomar fotos y acceder a la cámara",
            permission = Manifest.permission.CAMERA,
            activityClass = CameraActivity::class.java
        ),
        PermissionItem(
            name = "Galería",
            description = "Acceder a imágenes almacenadas",
            permission = Manifest.permission.READ_MEDIA_IMAGES,
            activityClass = GalleryActivity::class.java
        ),
        PermissionItem(
            name = "Micrófono",
            description = "Grabar audio con el micrófono",
            permission = Manifest.permission.RECORD_AUDIO,
            activityClass = AudioActivity::class.java
        ),
        PermissionItem(
            name = "Contactos",
            description = "Leer lista de contactos",
            permission = Manifest.permission.READ_CONTACTS,
            activityClass = ContactsActivity::class.java
        ),
        PermissionItem(
            name = "Teléfono",
            description = "Realizar llamadas telefónicas",
            permission = Manifest.permission.CALL_PHONE,
            activityClass = PhoneActivity::class.java
        ),
        PermissionItem(
            name = "Ubicación",
            description = "Obtener ubicación aproximada",
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            activityClass = LocationActivity::class.java
        ),
        PermissionItem(
            name = "Protección de Datos",
            description = "Ver logs y protección de datos",
            permission = null,
            activityClass = DataProtectionActivity::class.java
        ),

        // Nuevo item para notificaciones
        PermissionItem(
            name = "Notificaciones de Acceso a Datos Sensibles",
            description = "Recibir notificaciones cuando se accede a datos sensibles.",
            permission = null,
            activityClass = DataAccessNotificationActivity::class.java
        ),

        // Nuevo item para exportar logs
        PermissionItem(
            name = "Exportar Logs de Acceso",
            description = "Exportar logs de acceso de datos sensibles.",
            permission = null,
            activityClass = ExportLogsActivity::class.java
        ),

        // Nuevo item para la configuración de privacidad
        PermissionItem(
            name = "Configuración de Privacidad",
            description = "Administrar configuraciones de privacidad de la aplicación.",
            permission = null,
            activityClass = PrivacySettingsActivity::class.java
        ),

        // Nuevo item para el tutorial
        PermissionItem(
            name = "Tutorial de Permisos",
            description = "Ver un tutorial sobre cómo otorgar permisos a la aplicación.",
            permission = null,
            activityClass = PermissionTutorialActivity::class.java
        ),

        // Nuevo item para autenticación biométrica
        PermissionItem(
            name = "Autenticación Biométrica",
            description = "Configurar autenticación biométrica para acceder a la aplicación.",
            permission = null,
            activityClass = BiometricAuthenticationActivity::class.java
        ),

        PermissionItem(
            name = "Política de Privacidad",
            description = "Política de privacidad y términos",
            permission = null,
            activityClass = PrivacyPolicyActivity::class.java
        )




    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val permission = currentRequestedPermission
        if (permission != null) {
            permission.status = if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED
            permissionsAdapter.updatePermissionStatus(permission)

            val status = if (isGranted) "OTORGADO" else "DENEGADO"
            dataProtectionManager.logAccess("PERMISSION", "${permission.name}: $status")

            if (isGranted) {
                openActivity(permission)
            }
            currentRequestedPermission = null
        }
    }

    private var currentRequestedPermission: PermissionItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        updatePermissionStatuses()

        dataProtectionManager.logAccess("NAVIGATION", "MainActivity abierta")
    }

    private fun setupRecyclerView() {
        permissionsAdapter = PermissionsAdapter(permissions) { permission ->
            handlePermissionClick(permission)
        }

        binding.rvPermissions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = permissionsAdapter
        }
    }

    private fun updatePermissionStatuses() {
        permissions.forEach { permission ->
            permission.permission?.let { perm ->
                permission.status = when (ContextCompat.checkSelfPermission(this, perm)) {
                    PackageManager.PERMISSION_GRANTED -> PermissionStatus.GRANTED
                    PackageManager.PERMISSION_DENIED -> {
                        if (shouldShowRequestPermissionRationale(perm)) {
                            PermissionStatus.DENIED
                        } else {
                            PermissionStatus.NOT_REQUESTED
                        }
                    }
                    else -> PermissionStatus.NOT_REQUESTED
                }
            } ?: run {
                // Para actividades sin permisos específicos (como protección de datos)
                permission.status = PermissionStatus.GRANTED
            }
        }
        permissionsAdapter.notifyDataSetChanged()
    }

    private fun handlePermissionClick(permission: PermissionItem) {
        when {
            permission.permission == null -> {
                // Actividad sin permiso específico
                openActivity(permission)
            }
            permission.status == PermissionStatus.GRANTED -> {
                openActivity(permission)
            }
            permission.status == PermissionStatus.NOT_REQUESTED -> {
                requestPermission(permission)
            }
            permission.status == PermissionStatus.DENIED -> {
                // Mostrar explicación y pedir que vaya a configuración
                openActivity(permission) // Permitir que la actividad maneje el caso
            }
        }
    }

    private fun requestPermission(permission: PermissionItem) {
        permission.permission?.let { perm ->
            currentRequestedPermission = permission
            dataProtectionManager.logAccess("PERMISSION", "${permission.name}: SOLICITADO")
            requestPermissionLauncher.launch(perm)
        }
    }

    private fun openActivity(permission: PermissionItem) {
        permission.activityClass?.let { activityClass ->
            val intent = Intent(this, activityClass)
            startActivity(intent)
            dataProtectionManager.logAccess("NAVIGATION", "${permission.name} actividad abierta")
        }
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStatuses()
    }

    val CHANNEL_ID = "data_access_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Data Access Notifications"
            val descriptionText = "Notifications for sensitive data access"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDataAccessNotification(context: Context, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Acceso a Datos Sensibles")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

}