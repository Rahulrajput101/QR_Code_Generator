package com.ondevop.qrcodegenerator.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.nio.ByteBuffer

object QrUtility {

    fun hasCameraPermission(context : Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


     fun generateQrCode(text: String): Bitmap? {
        val width = 800
        val height = 800
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix =
                qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                    )
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }


    fun ByteBuffer.toByteArray() : ByteArray{
       rewind()
        return ByteArray(remaining()).also {
            get(it)
        }


    }

}