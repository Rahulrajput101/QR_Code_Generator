package com.ondevop.qrcodegenerator.utils

import android.graphics.Bitmap
import androidx.camera.core.Camera
import com.ondevop.qrcodegenerator.db.QrData

sealed interface MainUiEvents{
     object AddQr : MainUiEvents
     object shareQr : MainUiEvents
     data class DeleteQr(val qrData : QrData) : MainUiEvents
     



}