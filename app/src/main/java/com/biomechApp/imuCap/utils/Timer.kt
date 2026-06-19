package com.biomechApp.imuCap.utils

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LifecycleOwner
import com.biomechApp.imuCap.model.Recording
import com.biomechApp.imuCap.model.Streaming
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import kotlin.Int

class Timer(val sensorListViewModel: SensorListViewModel,
            val calibViewModel: CalibrationViewModel,
            val dataViewModel: DataViewModel,
            outputRate: String,
            val lifecycleOwner: LifecycleOwner,
            val appContext: Context,
            val recordingViewModel: RecordingViewModel,
            val configViewModel: ConfigViewModel,
            val avatarViewModel: AvatarViewModel,
            val dataWriter: Writer
) {
    private val outputRateValue = Regex("\\d+").find(outputRate)?.value!!.toInt()
    private val excludedSegments = listOf("None", "pelvis", "right hand", "left hand")
    private val segmentCount: Int = sensorListViewModel.segmentSelected.count { it !in excludedSegments }
    private val segmentSelected = sensorListViewModel.segmentSelected.filter { it !in excludedSegments }
    private val reversedSegmentSelected = segmentSelected.reversed()
    private val dynamicTime: Long = 5000L * segmentCount
    private val phasesIncrement: Float = 1F / segmentCount
    private val phases: MutableList<Float> = computePhases()


    private fun computePhases(): MutableList<Float> {
        val phases: MutableList<Float> = mutableListOf()
        for (i in 1 until segmentCount) {
            phases.add(i*phasesIncrement)
        }
        return phases
    }

    private fun checkValue(value: Double): String {
        var segmentName = ""

        if (phases.isEmpty()) {
            segmentName = reversedSegmentSelected[0]
        }
        else {
            when {
                value < phases.first() -> {
                    segmentName = reversedSegmentSelected[0]
                }

                value >= phases.last() -> {
                    segmentName = reversedSegmentSelected[segmentCount-1]
                }

                else -> {
                    for (i in 0 until phases.size - 1) {
                        val lower = phases[i]
                        val upper = phases[i + 1]

                        if (value >= lower && value < upper) {
                            segmentName = reversedSegmentSelected[i+1]
                            break
                        }
                    }
                }
            }
        }
        return segmentName
    }

    fun runSensorCalib(){
        object : CountDownTimer(10000, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if (!sensorListViewModel.recordingState) {
                    sensorListViewModel.onCleanBuffer()
                    sensorListViewModel.onRecordingTypeChange("calib")
                    Recording(sensorListViewModel, configViewModel).startMeasurement()
                }
                val progress = 1.0-(millisUntilFinished/10000.0)
                calibViewModel.onSensorCalibProgressChange(progress.toFloat())
                if (progress >0.5 && !sensorListViewModel.resetHeadingDone) {
                    sensorListViewModel.resetHeadingDone = true
                    sensorListViewModel.resetHeading()
                }
            }
            override fun onFinish() {
                calibViewModel.onShowSensorCalibProgressChange(false)
                sensorListViewModel.onRecordingTypeChange("standby")
                sensorListViewModel.resetHeadingDone = false
                sensorListViewModel.calibDataEmitter(
                    calibViewModel,
                    configViewModel,
                    outputRateValue
                )
            }
        }.start()
    }
    fun stopCalibRecording(){
        object : CountDownTimer(100, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                Recording(sensorListViewModel, configViewModel).stopMeasurement()
                sensorListViewModel.onRecordingTypeChange("null")
            }
        }.start()
    }
    fun staticTimer() {
        object : CountDownTimer(5000, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = 1.0-(millisUntilFinished/5000.0)
                calibViewModel.onStaticProgressChange(progress.toFloat())
            }
            override fun onFinish() {
                calibViewModel.onShowStaticChange(false)
                sensorListViewModel.onRecordingTypeChange("standby")
                sensorListViewModel.staticDataEmitter(outputRateValue)
                runDynamic()
            }
        }.start()
    }

    fun runDynamic(){
        object : CountDownTimer(500, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                calibViewModel.onShowDynamicChange(true)
                sensorListViewModel.onCleanBuffer()
                sensorListViewModel.onRecordingTypeChange("calib")
                dynamicTimer()
            }
        }.start()
    }

    fun dynamicTimer() {
        object : CountDownTimer(dynamicTime, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = 1.0-(millisUntilFinished/dynamicTime.toDouble())
                calibViewModel.onDynamicProgressChange(progress.toFloat())
                calibViewModel.onSegmentToBendChange(checkValue(progress))
            }
            override fun onFinish() {
                sensorListViewModel.onRecordingTypeChange("standby")
                sensorListViewModel.dynamicDataEmitter(outputRateValue, calibViewModel, configViewModel, segmentCount, reversedSegmentSelected)
                calibViewModel.onShowDynamicChange(false)
                if (!calibViewModel.dynamicCalibError) {
                    calibViewModel.onShowStopButtonChange(true)
                    runStreaming()
                }
            }
        }.start()
    }

    fun runStreaming(){
        object : CountDownTimer(2000, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                val streaming = Streaming(sensorListViewModel, dataViewModel, recordingViewModel, avatarViewModel, appContext, dataWriter)
                streaming.streamingOn(lifecycleOwner)
            }
        }.start()
    }
}
