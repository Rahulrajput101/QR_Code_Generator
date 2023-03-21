package com.ondevop.qrcodegenerator.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="qr_data"
)
data class QrData(
    var bitmap: Bitmap? =null,
    var result: String? ="",
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
