package com.example.seguridad_priv_a

import android.content.Context
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seguridad_priv_a.databinding.ActivityPrivacySettingsBinding

class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacySettingsBinding

    // Nombre del archivo de SharedPreferences
    private val PREFS_NAME = "privacy_prefs"
    private val PRIVACY_KEY = "isPrivacyEnabled"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración inicial de los switches
        initializePrivacySettings()

        // Listener para el Switch de privacidad
        binding.switchPrivacy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Activar la protección de privacidad
                savePrivacySetting(true)
                Toast.makeText(this, "Protección de privacidad activada", Toast.LENGTH_SHORT).show()
            } else {
                // Desactivar la protección de privacidad
                savePrivacySetting(false)
                Toast.makeText(this, "Protección de privacidad desactivada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializePrivacySettings() {
        // Obtenemos el estado guardado en SharedPreferences y actualizamos el switch
        val isPrivacyEnabled = getPrivacySettingFromPreferences()
        binding.switchPrivacy.isChecked = isPrivacyEnabled
    }

    // Función para guardar el estado de privacidad en SharedPreferences
    private fun savePrivacySetting(isEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(PRIVACY_KEY, isEnabled)
        editor.apply()  // Guardamos los cambios
    }

    // Función para obtener el estado de privacidad de SharedPreferences
    private fun getPrivacySettingFromPreferences(): Boolean {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(PRIVACY_KEY, true) // Default es true (activado)
    }
}
