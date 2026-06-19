package com.biomechApp.imuCap.model

import androidx.compose.runtime.mutableStateListOf
import com.biomechApp.imuCap.utils.Drift
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData

class CheckDrift(
    val data: List<List<DotData>>,
    val sensorListViewModel: SensorListViewModel,
    private val calibViewModel: CalibrationViewModel,
    val configViewModel: ConfigViewModel,
    private val rate: Int
) {
    private val drift = Drift()

    fun run(): Boolean {

        val sensorsDrift = mutableStateListOf<Boolean>()
        for (i in data.indices) {
            sensorsDrift.add(drift.checkDrift(data[i], rate))
        }
        return if (sensorsDrift.any { it }){
            sensorListViewModel.onCalibResultDriftChange(true)
            true
        } else{
            false
        }
    }
}