package com.ondevop.qrcodegenerator.utils

sealed interface MainUiEvents{
     object SaveQr : MainUiEvents
     object shareQr : MainUiEvents
      object getQr : MainUiEvents

}