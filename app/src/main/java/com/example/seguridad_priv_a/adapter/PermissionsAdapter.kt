package com.example.seguridad_priv_a.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seguridad_priv_a.R
import com.example.seguridad_priv_a.data.PermissionHelper
import com.example.seguridad_priv_a.data.PermissionItem
import com.example.seguridad_priv_a.databinding.ItemPermissionBinding

class PermissionsAdapter(
    private val permissions: List<PermissionItem>,
    private val onItemClick: (PermissionItem) -> Unit
) : RecyclerView.Adapter<PermissionsAdapter.PermissionViewHolder>() {

    inner class PermissionViewHolder(private val binding: ItemPermissionBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(permission: PermissionItem) {
            binding.apply {
                tvPermissionName.text = permission.name
                tvPermissionDescription.text = permission.description
                tvPermissionStatus.text = PermissionHelper.getStatusText(permission.status)

                ivPermissionIcon.setImageResource(getIconForPermission(permission))

                root.setOnClickListener {
                    onItemClick(permission)
                }
            }
        }

        // Función para obtener el ícono dependiendo del permiso
        private fun getIconForPermission(permission: PermissionItem): Int {
            return when (permission.name) {
                "Cámara" -> R.drawable.ic_camera
                "Galería" -> R.drawable.ic_gallery
                "Micrófono" -> R.drawable.ic_microphone
                "Contactos" -> R.drawable.ic_contacts
                "Teléfono" -> R.drawable.ic_phone
                "Ubicación" -> R.drawable.ic_location
                "Protección de Datos" -> R.drawable.ic_protection
                "Política de Privacidad" -> R.drawable.ic_privacy_policy
                "Autenticación Biométrica" -> R.drawable.ic_biometric_authentication  // Nuevo ícono
                "Exportar Logs" -> R.drawable.ic_export_logs  // Nuevo ícono
                "Notificación de Acceso a Datos" -> R.drawable.ic_data_access_notification  // Nuevo ícono
                "Configuración de Privacidad" -> R.drawable.ic_privacy_settings  // Nuevo ícono
                "Tutorial de Permisos" -> R.drawable.ic_permission_tutorial  // Nuevo ícono
                else -> R.drawable.ic_default_permission // ícono por defecto
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val binding = ItemPermissionBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return PermissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(permissions[position])
    }

    override fun getItemCount(): Int = permissions.size
    
    fun updatePermissionStatus(permission: PermissionItem) {
        val index = permissions.indexOf(permission)
        if (index != -1) {
            notifyItemChanged(index)
        }
    }
}