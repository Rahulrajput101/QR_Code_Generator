package com.ondevop.qrcodegenerator.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface QrDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertQr(qrData: QrData)

     @Query("SELECT * FROM qr_data")
     fun getQr() : LiveData<List<QrData>>


}