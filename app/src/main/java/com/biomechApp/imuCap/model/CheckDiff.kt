package com.biomechApp.imuCap.model

import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData

class CheckDiff(
    val data: List<List<DotData>>,
    val sensorListViewModel: SensorListViewModel,
    private val calibViewModel: CalibrationViewModel,
    val configViewModel: ConfigViewModel
) {
    private var shortestIndex = -1
    private var minLength = Int.MAX_VALUE

    private var largestIndex = -1
    private var maxLength = Int.MIN_VALUE

    private  val badSegments: ArrayList<String> = arrayListOf()

    fun run(): Boolean {

        for ((index, sublist) in data.withIndex()) {
            if (sublist.isNotEmpty() && sublist.size < minLength) {
                shortestIndex = index
                minLength = sublist.size
            }
        }

        for ((index, sublist) in data.withIndex()) {
            if (sublist.isNotEmpty() && sublist.size > maxLength) {
                largestIndex = index
                maxLength = sublist.size
            }
        }

        val result = maxLength - minLength

        for ((index, sublist) in data.withIndex()) {
            if (sublist.isNotEmpty() && (maxLength - sublist.size > 10)) {
                when (index){
                    0 -> {badSegments.add("Pelvis")}
                    1 -> {badSegments.add("Torso")}
                    2 -> {badSegments.add("Head")}
                    3 -> {badSegments.add("Right Thigh")}
                    4 -> {badSegments.add("Left Thigh")}
                    5 -> {badSegments.add("Right Shank")}
                    6 -> {badSegments.add("Left Shank")}
                    7 -> {badSegments.add("Right Foot")}
                    8 -> {badSegments.add("Left Foot")}
                    9 -> {badSegments.add("Right Arm")}
                    10 -> {badSegments.add("Left Arm")}
                    11 -> {badSegments.add("Right Forearm")}
                    12 -> {badSegments.add("Left Forearm")}
                    13 -> {badSegments.add("Right Hand")}
                    14 -> {badSegments.add("Left Hand")}
                }
            }
        }

        if (result > 10) {
            Recording(sensorListViewModel, configViewModel).stopMeasurement()
            sensorListViewModel.onRecordingTypeChange("null")
            sensorListViewModel.onBadSegmentsChange(badSegments)
            sensorListViewModel.onCalibResultDifChange(true)
            calibViewModel.onShowStartButtonChange(true)
            return true
        }else{
            return false
        }
    }
}