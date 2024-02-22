package com.kursatmemis.instagram_clone.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kursatmemis.instagram_clone.model.SelectImageResult
import javax.inject.Inject

class UploadImageFromGalleryUtil @Inject constructor() {

    fun selectImage(activity: Activity, context: Context): SelectImageResult {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Sürümü 33'den büyük olan cihazlarda resim seç.
            return selectImageOnDevicesWithVersionGreaterThan33(activity, context)
        } else {
            // Sürümü 33'den küçük olan cihazlarda resim seç.
            return selectImageOnDevicesWithVersionLessThan33(activity, context)
        }
    }

    private fun selectImageOnDevicesWithVersionLessThan33(activity: Activity, context: Context): SelectImageResult {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Izin verilmemiş.
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Izin isterken kullanıcıya bir mantık göstermeliyiz. (İznin neden istendiğini açıkla)
                return SelectImageResult.PERMISSION_DENIED_WITH_RATIONALE

            } else {
                // Izin isterken kullanıcıya bir mantık göstermemize gerek yok.
                return SelectImageResult.PERMISSION_DENIED_WITHOUT_RATIONALE
            }
        } else {
            // Izin verilmiş.
            return SelectImageResult.PERMISSION_GRANTED

        }
    }

    // SDK Version numarası 33'den büyük olan cihazlar için bu method kullanılarak resim seçtirilir.
    private fun selectImageOnDevicesWithVersionGreaterThan33(activity: Activity, context: Context): SelectImageResult {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Izin verilmemiş.
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                // Izin istersen kullanıcıya bir mantık göstermeliyiz. (İznin neden istendiğini açıkla)
                return SelectImageResult.PERMISSION_DENIED_WITH_RATIONALE
            } else {
                // Izin istersen kullanıcıya bir mantık göstermemize gerek yok.
                return SelectImageResult.PERMISSION_DENIED_WITHOUT_RATIONALE
            }
        } else {
            // Izin verilmiş.
            return SelectImageResult.PERMISSION_GRANTED
        }
    }

}

