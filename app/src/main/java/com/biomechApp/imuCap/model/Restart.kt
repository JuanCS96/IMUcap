package com.biomechApp.imuCap.model

import com.biomechApp.imuCap.viewModel.SensorListViewModel

class Restart(val sensorListViewModel: SensorListViewModel) {

    fun startBad(){
        val segments = sensorListViewModel.badSegments.split(", ")

        val badAddress = sensorListViewModel.connectionLiveList.filter { it.segment in segments }.map { it.address }

        for (addr in badAddress){
            val dotSensor = sensorListViewModel.sensorLiveList.find { it.address == addr }
            dotSensor?.disconnect()
        }
    }
}