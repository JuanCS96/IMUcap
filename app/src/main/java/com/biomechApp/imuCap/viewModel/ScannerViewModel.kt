package com.biomechApp.imuCap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScannerViewModel: ViewModel() {

    private val _scannerState = MutableLiveData<Boolean>()
    val scannerState : LiveData<Boolean> = _scannerState

    fun onScannerStateChange(scannerState:Boolean){
        _scannerState.value = scannerState
    }
}