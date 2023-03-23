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
import android.util.TypedValue
import android.widget.Toast
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

    fun hasCameraPermission(context: Context) =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED


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


    fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }


    }

    //This Helper function checks the device is running on sdk 29 or above or else.
    inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onSdk29()
        } else null

    }


    //This method used to store the Qr code in external memory
    fun saveQrToExternalStorage(context: Context, displayText: String, bitmap: Bitmap): Uri? {

        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayText.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, "${bitmap.width}")
            put(MediaStore.Images.Media.HEIGHT, "${bitmap.height}")
        }

        return try {
            val contentResolver = context.contentResolver
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outPutstream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outPutstream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun shareQr(qrUri: Uri?) : Intent?{
          qrUri?.let {
              return Intent(Intent.ACTION_SEND).also {
                  it.type = "image/jpeg"
                  // This will attach the QR code image to the share intent
                  it.putExtra(Intent.EXTRA_STREAM, qrUri)
                  it.putExtra(Intent.EXTRA_TEXT, "Hey, I have created this amazing QR code")
              }
          } ?: return null
    }


    fun provideBackgrounColorPrimary(context : Context) : Int {
        val primaryColorAttr = android.R.attr.colorPrimary
        val primaryColorValue = TypedValue()
        context.theme.resolveAttribute(primaryColorAttr, primaryColorValue, true)
        return primaryColorValue.data
    }




}