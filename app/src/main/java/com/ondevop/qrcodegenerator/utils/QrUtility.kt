package com.ondevop.qrcodegenerator.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri

import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

object QrUtility {

    fun hasCameraPermission(context : Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED



     fun generateQrCode(text: String): Bitmap? {
        val width = 800
        val height = 800
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
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

    //This Helper function checks the device is running on sdk 29 or above or else.
    inline fun <T> sdk29AndUp(onSdk29 : () -> T): T? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            onSdk29()
        } else null

    }


//    fun shareQr( displayText: String){
//
//        // Get a File object for the QR code image
//        val qrCodeFile = File(Environment.getExternalStorageDirectory(), "$displayText.jpg")
//
//        // Create a share intent with the ACTION_SEND action
//        val shareIntent = Intent(Intent.ACTION_SEND)
//
//        // Set the MIME type of the content to "image/*"
//        shareIntent.type = "image/*"
//
//        // Attach the QR code image to the share intent
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(qrCodeFile.absolutePath))
//
//        // Launch the share dialog
//        startActivity(Intent.createChooser(shareIntent, "Share QR code"))
//
//    }




}