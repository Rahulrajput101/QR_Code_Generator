package com.ondevop.qrcodegenerator.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [QrData::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class QrDatabase : RoomDatabase(){

    abstract val qrDao : QrDao

    companion object{
        const val QR_DATABASE_NAME ="qr_db"
    }
}