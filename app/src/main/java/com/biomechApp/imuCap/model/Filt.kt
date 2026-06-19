package com.biomechApp.imuCap.model

import android.os.CountDownTimer
import com.biomechApp.imuCap.viewModel.SensorListViewModel

class Filt(
    val sensorListViewModel: SensorListViewModel,
    outputRate: String,
    private val filter: String
) {
    val outputRateValue = Regex("\\d+").find(outputRate)?.value!!.toInt()

    fun change(){
        object : CountDownTimer(2000, ((1.0/outputRateValue)*1000).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                sensorListViewModel.onXsensDotFilterProfileChange(filter)
            }
        }.start()
    }
}