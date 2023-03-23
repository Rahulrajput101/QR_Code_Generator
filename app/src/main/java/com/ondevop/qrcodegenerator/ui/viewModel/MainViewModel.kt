package com.ondevop.qrcodegenerator.ui.viewModel

import android.graphics.Bitmap
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

    var isVisible = MutableLiveData<Boolean>()

    val savedQrData = repository.getQr()

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()



    fun onEvent(event : MainUiEvents){
        when(event){

           is  MainUiEvents.AddQr -> {
                viewModelScope.launch {
                        repository.insertQr(
                            QrData(
                                bitmap = bitmap.value!!,
                                result = scanResult.value!!
                            )
                        )
                    _eventFlow.send(UiEvent.ShowSnackbar("Added successfully"))
                }
            }

           is  MainUiEvents.DeleteQr -> {
                viewModelScope.launch {
                    repository.deleteQr(event.qrData)
                    _eventFlow.send(UiEvent.ShowSnackbar("Item is Deleted successfully"))

                }
            }

        }

    }

    fun setBitmapValue(newBitmap: Bitmap){
        _bitmap.value = newBitmap
    }

    fun setScanResult(newResult : String){
        _scanResult.value =newResult
    }

    fun setVisibility(visible : Boolean){
        isVisible.value = visible
    }




    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()

    }






}