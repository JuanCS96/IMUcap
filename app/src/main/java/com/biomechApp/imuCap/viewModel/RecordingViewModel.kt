package com.biomechApp.imuCap.viewModel

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordingViewModel: ViewModel() {
    private val _recordingState = MutableLiveData<String>()
    val recordingState : LiveData<String> = _recordingState

    fun onRecordingStateChange(recordingState:String){
        _recordingState.value = recordingState
    }

    private val _recordingIcon = MutableLiveData<ImageVector>()
    val recordingIcon : LiveData<ImageVector> = _recordingIcon

    fun onRecordingIconChange(icon:ImageVector){
        _recordingIcon.value = icon
    }

    private val _showIsStreaming = MutableLiveData<Boolean>()
    val showIsStreaming : LiveData<Boolean> = _showIsStreaming

    fun onShowIsStreamingChange(streaming:Boolean){
        _showIsStreaming.value = streaming
    }

    private val _showStopRecording = MutableLiveData<Boolean>()
    val showStopRecording : LiveData<Boolean> = _showStopRecording

    fun onShowStopRecordingChange(stopStreaming:Boolean){
        _showStopRecording.value = stopStreaming
    }

    private val _startWriting = MutableLiveData<Boolean>()
    val startWriting : LiveData<Boolean> = _startWriting

    fun onStartWritingChange(start:Boolean){
        _startWriting.value = start
    }

    private val _recordingSwitch = MutableLiveData<Int>()
    val recordingSwitch : LiveData<Int> = _recordingSwitch

    fun onRecordingSwitchChange(switch:Int){
        _recordingSwitch.value = switch
    }

    private val _showRecordingSwitchInfo = MutableLiveData<Boolean>()
    val showRecordingSwitchInfo : LiveData<Boolean> = _showRecordingSwitchInfo

    fun onShowRecordingSwitchInfoChange(state: Boolean){
        _showRecordingSwitchInfo.value = state
    }

    private val _videoRecordingState = MutableLiveData<Boolean>()
    val videoRecordingState : LiveData<Boolean> = _videoRecordingState

    fun onVideoRecordingStateChange(state: Boolean){
        _videoRecordingState.value = state
    }

    private val _videoRecordingInfo = MutableLiveData<Boolean>()
    val videoRecordingInfo : LiveData<Boolean> = _videoRecordingInfo

    fun onVideoRecordingInfoChange(state: Boolean){
        _videoRecordingInfo.value = state
    }
}