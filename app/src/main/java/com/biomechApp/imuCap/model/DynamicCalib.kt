package com.biomechApp.imuCap.model

import com.biomechApp.imuCap.utils.Filter
import com.biomechApp.imuCap.utils.KeyPoints
import com.biomechApp.imuCap.utils.Operations
import com.biomechApp.imuCap.utils.SenToSeg
import com.biomechApp.imuCap.utils.TimeRanges
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData
import kotlin.Int
import kotlin.collections.List
import kotlin.math.pow
import kotlin.math.sqrt

class DynamicCalib (
    data: List<MutableList<DotData>>,
    val sensorListViewModel: SensorListViewModel,
    calibViewModel: CalibrationViewModel,
    configViewModel: ConfigViewModel,
    outputRate: Int,
    segmentCount: Int,
    reversedSegmentSelected: List<String>
) {
    private var shortestIndex = -1
    private var minLength = Int.MAX_VALUE
    private val keyPoints = KeyPoints()
    private val sen2seg = SenToSeg()
    private val operations = Operations()

    init {

        for ((index, sublist) in data.withIndex()) {
            if (sublist.isNotEmpty() && sublist.size < minLength) {
                shortestIndex = index
                minLength = sublist.size
            }
        }

        val segmentRanges = TimeRanges().run(segmentCount, reversedSegmentSelected, outputRate, minLength)

        val gyrAbs = MutableList(15) { mutableListOf<Double>() }
        for (i in data.indices) {
            if (data[i].isNotEmpty()) {
                gyrAbs[i] = angVelAbs(data[i], minLength)
            }
        }

        val instants = mutableListOf<Int>()
        instants.add(-1)
        for (i in 1 until gyrAbs.size) {
            if (gyrAbs[i].isNotEmpty()) {
                instants.add(keyPoints.run(gyrAbs, i, outputRate, segmentRanges))
            }
            else {
                instants.add(-1)
            }
        }

        if (instants.any { it == 0 }) {

            Recording(sensorListViewModel, configViewModel).stopMeasurement()
            sensorListViewModel.onRecordingTypeChange("null")
            calibViewModel.onDynamicCalibErrorChange(true)
            calibViewModel.onShowBadModelCalibLevelChange(true)
            calibViewModel.onShowStartButtonChange(true)

        } else {

            val localsQ = mutableListOf<List<Float>?>()
            val localsYaxis = mutableListOf<List<Float>?>()
            for (i in 1 until instants.size) {
                if (instants[i] != -1) {
                    val (localQ, localYaxis) = sen2seg.run(data[i][instants[i]], i, sensorListViewModel)
                    localsQ.add(localQ)
                    localsYaxis.add(localYaxis)
                }
                else {
                    localsQ.add(null)
                    localsYaxis.add(null)
                }
            }

            var pelvisQ: List<Float> = listOf()
            if (data[0].isNotEmpty()) {
                if (localsQ[0] != null) {
                    pelvisQ = localsQ[0]!!
                }
                else if (localsQ[2] != null && localsQ[3] != null) {
                    val pelvisYaxis: List<Float> = operations.vectUni(
                        listOf(
                            (localsYaxis[2]!![0] + localsYaxis[3]!![0])/2,
                            (localsYaxis[2]!![1] + localsYaxis[3]!![1])/2,
                            (localsYaxis[2]!![2] + localsYaxis[3]!![2])/2
                        )
                    )

                    val pelvisXaxis = operations.vectUni(operations.crossProd(pelvisYaxis, listOf(0F, 0F, 1F)))

                    val r00 = pelvisXaxis[0]; val r01 = pelvisYaxis[0]; val r02 = 0f
                    val r10 = pelvisXaxis[1]; val r11 = pelvisYaxis[1]; val r12 = 0f
                    val r20 = pelvisXaxis[2]; val r21 = pelvisYaxis[2]; val r22 = 1f

                    pelvisQ = operations.rotationMatrixToQuaternion(r00, r01, r02, r10, r11, r12, r20, r21, r22).toList()
                }
                else if (localsQ[2] != null) {
                    pelvisQ = localsQ[2]!!
                }
                else if (localsQ[3] != null) {
                    pelvisQ = localsQ[3]!!
                }

                localsQ.add(0, pelvisQ)
            }
            else {
                localsQ.add(0, null)
            }

            val localFrames: List<List<Float>> = localsQ.map { it ?: emptyList() }

            sensorListViewModel.onLocalFramesChange(localFrames)

            val filterIni = Filter().zero(sensorListViewModel)
            if (filterIni.any { it > 30.0 || it < -30.0 }) {
                calibViewModel.onShowPoorModelCalibLevelChange(true)
            }
            else {
                calibViewModel.onShowGoodModelCalibLevelChange(true)
            }
        }
    }

    private fun angVelAbs(data: List<DotData>, minLength: Int): MutableList<Double> {

        val gyrSensor: MutableList<DoubleArray> = mutableListOf()
        for (i in 0 until minLength){
            try {
                gyrSensor.add(data[i].gyr)
            }catch (e: Exception){
                continue
            }

        }

        val gyrSensorAbs: MutableList<Double> = mutableListOf()
        for (i in 0 until gyrSensor.size){
            gyrSensorAbs.add(
                sqrt(
                    gyrSensor[i][0].pow(2) +
                    gyrSensor[i][1].pow(2) +
                    gyrSensor[i][2].pow(2)
                )
            )
        }
        return gyrSensorAbs
    }
}
