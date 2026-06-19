package com.biomechApp.imuCap.model

import android.util.Log
import com.biomechApp.imuCap.utils.Operations
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData

class StaticCalib (
    private val data: List<MutableList<DotData>>,
    val sensorListViewModel: SensorListViewModel,
    outputRate: Int
) {

    private var shortestIndex = -1
    private var minLength = Int.MAX_VALUE
    private val operations = Operations()

    init {

        for ((index, sublist) in data.withIndex()) {
            if (sublist.isNotEmpty() && sublist.size < minLength) {
                shortestIndex = index
                minLength = sublist.size
            }
        }

        val quatData = List(15) { mutableListOf<FloatArray>() }

        for (i in data.indices) {
            if (data[i].isNotEmpty()) {
                for (j in 0 until minLength) {
                    quatData[i].add(data[i][j].quat)
                }
            }
        }

        val meanQuatData = List(15) { mutableListOf<Float>() }

        for (i in quatData.indices) {
            if (quatData[i].isNotEmpty()) {
                val quatW: MutableList<Float> = mutableListOf()
                val quatX: MutableList<Float> = mutableListOf()
                val quatY: MutableList<Float> = mutableListOf()
                val quatZ: MutableList<Float> = mutableListOf()
                for (j in (outputRate * 2) until (minLength - outputRate * 2)) {
                    quatW.add(quatData[i][j][0])
                    quatX.add(quatData[i][j][1])
                    quatY.add(quatData[i][j][2])
                    quatZ.add(quatData[i][j][3])
                }
                meanQuatData[i].add(quatW.average().toFloat())
                meanQuatData[i].add(quatX.average().toFloat())
                meanQuatData[i].add(quatY.average().toFloat())
                meanQuatData[i].add(quatZ.average().toFloat())
            }
        }

        sensorListViewModel.onStaticSegmentsChange(meanQuatData)
    }
}