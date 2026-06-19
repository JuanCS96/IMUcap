package com.biomechApp.imuCap.model

import com.xsens.dot.android.sdk.models.DotDevice

data class Sensor(
    val address:String,
    var name:String,
    var battery:Int,
    var rssi:Int,
    val dotSensor: DotDevice,
    var segment: String
)