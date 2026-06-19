package com.biomechApp.imuCap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalibrationViewModel: ViewModel(){

    private val _showStartButton = MutableLiveData<Boolean>()
    val showStartButton : LiveData<Boolean> = _showStartButton

    private val _showSensorCalib = MutableLiveData<Boolean>()
    val showSensorCalib : LiveData<Boolean> = _showSensorCalib

    private val _showNoSensorCalibReady = MutableLiveData<Boolean>()
    val showNoSensorCalibReady : LiveData<Boolean> = _showNoSensorCalibReady

    private val _showStartModelCalib = MutableLiveData<Boolean>()
    val showStartModelCalib : LiveData<Boolean> = _showStartModelCalib

    private val _showSensorCalibProgress = MutableLiveData<Boolean>()
    val showSensorCalibProgress : LiveData<Boolean> = _showSensorCalibProgress

    private val _showJointsMismatch = MutableLiveData<Boolean>()
    val showJointsMismatch : LiveData<Boolean> = _showJointsMismatch

    private val _sensorCalibProgress = MutableLiveData<Float>()
    val sensorCalibProgress : LiveData<Float> = _sensorCalibProgress

    var dynamicCalibError : Boolean = false

    fun onShowStartButtonChange(showStart:Boolean){
        _showStartButton.value = showStart
    }

    fun onShowSensorCalibChange(showSensorCalib:Boolean){
        _showSensorCalib.value = showSensorCalib
    }

    fun onShowNoSensorCalibReadyChange(showNoSensorCalibReady:Boolean){
        _showNoSensorCalibReady.value = showNoSensorCalibReady
    }

    fun onShowStartModelCalibChange(showModelCalib:Boolean){
        _showStartModelCalib.value = showModelCalib
    }

    fun onShowSensorCalibProgressChange(showSensorCalibProgress:Boolean){
        _showSensorCalibProgress.value = showSensorCalibProgress
    }

    fun onShowJointsMismatchChange(jointsMismatch:Boolean){
        _showJointsMismatch.value = jointsMismatch
    }

    fun onSensorCalibProgressChange(sensorCalibProgress:Float){
        _sensorCalibProgress.value = sensorCalibProgress
    }

    fun onDynamicCalibErrorChange(error: Boolean){
        dynamicCalibError = error
    }

    private val _showStatic = MutableLiveData<Boolean>()
    val showStatic : LiveData<Boolean> = _showStatic

    private val _showDynamic = MutableLiveData<Boolean>()
    val showDynamic : LiveData<Boolean> = _showDynamic

    private val _showStopButton = MutableLiveData<Boolean>()
    val showStopButton : LiveData<Boolean> = _showStopButton

    private val _staticProgress = MutableLiveData<Float>()
    val staticProgress : LiveData<Float> = _staticProgress

    private val _dynamicProgress = MutableLiveData<Float>()
    val dynamicProgress : LiveData<Float> = _dynamicProgress

    private val _showNoSensorCalibDone = MutableLiveData<Boolean>()
    val showNoSensorCalibDone : LiveData<Boolean> = _showNoSensorCalibDone


    fun onShowStaticChange(showStatic:Boolean){
        _showStatic.value = showStatic
    }

    fun onShowDynamicChange(showDynamic:Boolean){
        _showDynamic.value = showDynamic
    }

    fun onShowStopButtonChange(showStop:Boolean){
        _showStopButton.value = showStop
    }

    fun onStaticProgressChange(staticProgress:Float){
        _staticProgress.value = staticProgress
    }

    fun onDynamicProgressChange(dynamicProgress:Float){
        _dynamicProgress.value = dynamicProgress
    }

    fun onNoSensorCalibDoneChange(showNoSensorCalibDone:Boolean){
        _showNoSensorCalibDone.value = showNoSensorCalibDone
    }

    private val _showGoodModelCalibLevel = MutableLiveData<Boolean>()
    val showGoodModelCalibLevel : LiveData<Boolean> = _showGoodModelCalibLevel

    fun onShowGoodModelCalibLevelChange(showGoodLevel:Boolean){
        _showGoodModelCalibLevel.value = showGoodLevel
    }

    private val _showPoorModelCalibLevel = MutableLiveData<Boolean>()
    val showPoorModelCalibLevel : LiveData<Boolean> = _showPoorModelCalibLevel

    fun onShowPoorModelCalibLevelChange(showPoorLevel:Boolean){
        _showPoorModelCalibLevel.value = showPoorLevel
    }

    private val _showBadModelCalibLevel = MutableLiveData<Boolean>()
    val showBadModelCalibLevel : LiveData<Boolean> = _showBadModelCalibLevel

    fun onShowBadModelCalibLevelChange(showBadLevel:Boolean){
        _showBadModelCalibLevel.value = showBadLevel
    }

    private val _segmentToBend = MutableLiveData<String>()
    val segmentToBend : LiveData<String> = _segmentToBend

    fun onSegmentToBendChange(segName: String){
        var bendingName = ""

        when (segName){
            "torso" -> {
                bendingName = "torso"
            }
            "head" -> {
                bendingName = "neck"
            }
            "right thigh" -> {
                bendingName = "right hip"
            }
            "left thigh" -> {
                bendingName = "left hip"
            }
            "right shank" -> {
                bendingName = "right knee"
            }
            "left shank" -> {
                bendingName = "left knee"
            }
            "right foot" -> {
                bendingName = "right ankle"
            }
            "left foot" -> {
                bendingName = "left ankle"
            }
            "right arm" -> {
                bendingName = "right shoulder"
            }
            "left arm" -> {
                bendingName = "left shoulder"
            }
            "right forearm" -> {
                bendingName = "right elbow"
            }
            "left forearm" -> {
                bendingName = "left elbow"
            }
            "right hand" -> {
                bendingName = "right wrist"
            }
            "left hand" -> {
                bendingName = "left wrist"
            }
        }
        _segmentToBend.value = "Bend $bendingName"
    }
}

