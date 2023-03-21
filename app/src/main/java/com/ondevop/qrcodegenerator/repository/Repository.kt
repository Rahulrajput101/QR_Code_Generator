package com.ondevop.qrcodegenerator.repository

import androidx.lifecycle.LiveData
import com.ondevop.qrcodegenerator.db.QrDao
import com.ondevop.qrcodegenerator.db.QrData
import javax.inject.Inject

class Repository(
    private val dao: QrDao
) {


    suspend fun insertQr(qrData: QrData) {
        return dao.insertQr(qrData)
    }

    fun getQr(): LiveData<List<QrData>> {
        return dao.getQr()
    }


}