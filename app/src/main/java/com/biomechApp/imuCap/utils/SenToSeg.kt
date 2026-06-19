package com.biomechApp.imuCap.utils

import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.events.DotData

class SenToSeg {
    private val operations = Operations()

    fun run(q: DotData, index: Int, sensorListViewModel: SensorListViewModel): Pair<List<Float>, List<Float>> {

        val quat = sensorListViewModel.staticSegments[index]
        val vel = (q.gyr).toList()

        val segRotUp = listOf(3, 4, 9, 10, 11, 12, 13, 14)

        val angVel = if (index in segRotUp) {
            mutableListOf(
                -vel[0].toFloat(),
                -vel[1].toFloat(),
                -vel[2].toFloat()
            )
            }else{
                mutableListOf(
                    vel[0].toFloat(),
                    vel[1].toFloat(),
                    vel[2].toFloat()
                )
            }
        
        val angVelUni = operations.vectUni(angVel)

        val angVelQ = angVelUni.toMutableList()
        angVelQ.add(0, 0F)

        val rot1 = operations.qProd(quat, angVelQ)
        val rot2 = operations.qProd(rot1, operations.qConj(quat))

        val yAxis = operations.vectUni(listOf(rot2[1], rot2[2], 0.0f))
        val xAxis = operations.vectUni(operations.crossProd(yAxis, listOf(0F, 0F, 1F)))

        val r00 = xAxis[0]; val r01 = yAxis[0]; val r02 = 0f
        val r10 = xAxis[1]; val r11 = yAxis[1]; val r12 = 0f
        val r20 = xAxis[2]; val r21 = yAxis[2]; val r22 = 1f

        val q = operations.rotationMatrixToQuaternion(r00, r01, r02, r10, r11, r12, r20, r21, r22)

        return Pair(q.toList(), yAxis)
    }
}