package com.biomechApp.imuCap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigViewModel: ViewModel() {

    private val _showAboutInfo = MutableLiveData<Boolean>()
    val showAboutInfo : LiveData<Boolean> = _showAboutInfo

    private val _showRecordingConfig = MutableLiveData<Boolean>()
    val showRecordingConfig : LiveData<Boolean> = _showRecordingConfig

    private val _showAbout = MutableLiveData<Boolean>()
    val showAbout : LiveData<Boolean> = _showAbout

    private val _showPowerOff = MutableLiveData<Boolean>()
    val showPowerOff : LiveData<Boolean> = _showPowerOff

    private val _showRecordingName = MutableLiveData<Boolean>()
    val showRecordingName : LiveData<Boolean> = _showRecordingName

    private val _recordingFileName = MutableLiveData<String>()
    val recordingFileName : LiveData<String> = _recordingFileName

    private val _recordingFileNameEnable = MutableLiveData<Boolean>()
    val recordingFileNameEnable : LiveData<Boolean> = _recordingFileNameEnable

    private val _showNoName = MutableLiveData<Boolean>()
    val showNoName : LiveData<Boolean> = _showNoName

    var fileOutName = ""

    private val _selectedHz = MutableLiveData<String>()
    val selectedHz : LiveData<String> = _selectedHz

    private val _selectedFilt = MutableLiveData<String>()
    val selectedFilt : LiveData<String> = _selectedFilt

    private val _appliedHz = MutableLiveData<String>()
    val appliedHz : LiveData<String> = _appliedHz

    private val _appliedFilt = MutableLiveData<String>()
    val appliedFilt : LiveData<String> = _appliedFilt

    fun onShowAboutInfoChange(about:Boolean){
        _showAboutInfo.value = about
    }

    fun onShowRecordingConfigChange(showRecordingConfig:Boolean){
        _showRecordingConfig.value = showRecordingConfig
    }

    fun onShowAboutChange(showAbout:Boolean){
        _showAbout.value = showAbout
    }

    fun onShowPowerOffChange(showPowerOff:Boolean){
        _showPowerOff.value = showPowerOff
    }

    fun onShowRecordingNameChange(showRecordingName:Boolean){
        _showRecordingName.value = showRecordingName
    }

    fun onRecordingFileNameChange(fileName:String){
        _recordingFileName.value =
            if (fileName.length <= 20) {
                fileName
            } else {
                _recordingFileName.value
            }
    }

    fun onRecordingFileNameEnableChange(enable:Boolean){
        _recordingFileNameEnable.value = enable
    }

    fun onFileOutChange(outName:String){
        fileOutName = outName
    }

    fun onShowNoNameChange(name:Boolean){
        _showNoName.value = name
    }

    fun onSelectedHzChange(selectedHz:String){
        _selectedHz.value = selectedHz
    }

    fun onSelectedFiltChange(selectedFilt:String){
        _selectedFilt.value = selectedFilt
    }

    fun onAppliedHzChange(appliedHz:String){
        _appliedHz.value = appliedHz
    }

    fun onAppliedFiltChange(appliedFilt:String){
        _appliedFilt.value = appliedFilt
    }
}