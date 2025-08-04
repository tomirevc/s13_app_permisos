package com.example.seguridad_priv_a

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.seguridad_priv_a.databinding.ActivityPermissionTutorialBinding

class PermissionTutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del tutorial
        setupTutorial()

        // Botón para continuar o cerrar el tutorial
        binding.btnContinue.setOnClickListener {
            // En este caso, simplemente cerramos la actividad al continuar
            finish()
        }
    }

    private fun setupTutorial() {
        // Aquí agregamos una lista de permisos y su descripción
        val permissionsText = """
            1. Cámara: Para tomar fotos y grabar videos.
            2. Micrófono: Para grabar sonidos.
            3. Galería: Para acceder a las fotos y videos almacenados en tu dispositivo.
            4. Almacenamiento: Para leer y escribir archivos en tu dispositivo.
            5. Contactos: Para acceder a la lista de tus contactos.
            6. Ubicación: Para obtener tu ubicación aproximada.
            7. Teléfono: Para realizar y gestionar llamadas.
        """.trimIndent()

        binding.tvPermissionsDescription.text = permissionsText
    }
}
