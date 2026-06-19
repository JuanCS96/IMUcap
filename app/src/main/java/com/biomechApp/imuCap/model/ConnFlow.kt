package com.biomechApp.imuCap.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnFlow(sensorListViewModel: SensorListViewModel,
               scannerViewModel: ScannerViewModel,
               lifecycleOwner: LifecycleOwner
) {
    init {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            while (true){
                if (sensorListViewModel.enableConnect && sensorListViewModel.connectionFlowList.size>0){
                    sensorListViewModel.onEnableConnectChange(false)
                    val dotSensorFlowList = sensorListViewModel.connectionFlowList[0].address
                    val dotSensorLiveList = sensorListViewModel.sensorLiveList.find { it.address == dotSensorFlowList }
                    dotSensorLiveList?.connect()
                }
            }
        }
    }
}