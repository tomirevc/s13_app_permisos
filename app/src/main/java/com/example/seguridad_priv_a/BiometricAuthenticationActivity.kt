package com.example.seguridad_priv_a

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.seguridad_priv_a.databinding.ActivityBiometricAuthenticationBinding
import java.util.concurrent.Executor

class BiometricAuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBiometricAuthenticationBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                binding.tvBiometricStatus.text = getString(R.string.biometric_success)
                showToast(getString(R.string.authentication_successful))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                binding.tvBiometricStatus.text = getString(R.string.biometric_failed)
                showToast(getString(R.string.authentication_failed))
            }
        })

        binding.btnAuthenticate.setOnClickListener {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
