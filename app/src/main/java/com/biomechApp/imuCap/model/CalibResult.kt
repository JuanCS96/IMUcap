package com.biomechApp.imuCap.model

import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData

class CalibResult(
    data: List<List<DotData>>,
    sensorListViewModel: SensorListViewModel,
    calibViewModel: CalibrationViewModel,
    configViewModel: ConfigViewModel,
    rate: Int
) {
    init {

        if (!CheckDiff(data, sensorListViewModel, calibViewModel, configViewModel).run()){
            sensorListViewModel.onCalibResultOkChange(true)
        }
    }
}