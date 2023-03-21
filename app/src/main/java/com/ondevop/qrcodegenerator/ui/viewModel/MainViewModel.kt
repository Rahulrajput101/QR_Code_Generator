package com.ondevop.qrcodegenerator.ui.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ondevop.qrcodegenerator.db.QrData
import com.ondevop.qrcodegenerator.repository.Repository
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap : LiveData<Bitmap> = _bitmap

    private val _scanResult = MutableLiveData<String>()
    val scanResult : LiveData<String> = _scanResult


    private val _savedQrData = MutableLiveData<List<QrData>>()
    val savedQrData : LiveData<List<QrData>> = _savedQrData

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getData()
    }



    fun setBitmapValue(newBitmap: Bitmap){
        _bitmap.value = newBitmap
    }

    fun setScanResult(newResult : String){
        _scanResult.value =newResult
    }

    fun onEvent(event : MainUiEvents){
        when(event){

            MainUiEvents.SaveQr -> {

                viewModelScope.launch {
                        repository.insertQr(
                            QrData(
                                bitmap = bitmap.value!!,
                                result = scanResult.value!!
                            )
                        )
                    _eventFlow.send(UiEvent.saveNote)
                }
            }
            MainUiEvents.shareQr -> {

            }
            MainUiEvents.getQr -> { }


        }

    }



    fun getData() {
        val response = repository.getQr()
        val item = response.value?.get(0)
        Log.d("get","${item?.result}")
        _savedQrData.postValue(response.value)

    }




    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object saveNote : UiEvent()
    }






}