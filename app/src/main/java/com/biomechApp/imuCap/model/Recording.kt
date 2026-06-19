package com.biomechApp.imuCap.model

import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

class Recording(
    private val sensorListViewModel: SensorListViewModel,
    val configViewModel: ConfigViewModel
) {
    fun startMeasurement(){
        sensorListViewModel.onRecordingStateChange(true)
        for (sensor in sensorListViewModel.sensorLiveList){
            sensor.startMeasuring()
        }
    }

    fun stopMeasurement(){
        sensorListViewModel.onRecordingStateChange(false)
        for (sensor in sensorListViewModel.sensorLiveList){
            sensor.stopMeasuring()
        }
    }
}