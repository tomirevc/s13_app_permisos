package com.example.seguridad_priv_a

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seguridad_priv_a.databinding.ActivityExportLogsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class ExportLogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExportLogsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el botón de exportación
        binding.btnExportLogs.setOnClickListener {
            exportLogs()
        }
    }

    private fun exportLogs() {
        try {
            // Aquí se obtiene el archivo de logs (esto es solo un ejemplo, personaliza según tu sistema de logs)
            val logs = getLogs()

            // Guardar el archivo en el almacenamiento del dispositivo
            val file = File(filesDir, "logs_export.txt")
            val fileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)

            outputStreamWriter.write(logs)
            outputStreamWriter.close()

            Toast.makeText(this, "Logs exportados correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al exportar los logs", Toast.LENGTH_SHORT).show()
        }
    }

    // Este es un ejemplo de cómo podrías obtener los logs (personaliza según tus necesidades)
    private fun getLogs(): String {
        return "Log 1: Acceso a datos sensibles.\n" +
                "Log 2: Permiso de cámara solicitado.\n" +
                "Log 3: Permiso de micrófono denegado.\n"
    }
}
