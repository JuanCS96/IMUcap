package com.biomechApp.imuCap.utils

import com.biomechApp.imuCap.viewModel.SensorListViewModel

class Filter {
    private val operations = Operations()
    private val q2e = Q2E()

    fun zero(sensorListViewModel: SensorListViewModel): List<Double> {

        val segQ90 = MutableList(15) { listOf<Float>() }
        for (i in 0 until sensorListViewModel.staticSegments.size) {
            val static = sensorListViewModel.staticSegments[i]
            val local = sensorListViewModel.localFrames[i]
            if (static.isNotEmpty() && local.isNotEmpty()) {
                val S2B = operations.qProd(operations.qConj(local), static)
                val segQ = operations.qProd(static, operations.qConj(S2B))
                segQ90[i] = operations.rot90x(segQ)
            }
        }

        val rotStatic: MutableList<Double> = MutableList(14) { 0.0 }
        for (joint in sensorListViewModel.jointSelected) {
            when (joint){
                "neck" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[2]), segQ90[1])
                    rotStatic[0] = q2e.joint(qJointStatic, listOf(1, -1, 1))[1].toDouble()
                }
                "lumbar" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[1]), segQ90[0])
                    rotStatic[1] = q2e.joint(qJointStatic, listOf(1, -1, 1))[1].toDouble()
                }
                "r_hip" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[3]), segQ90[0])
                    rotStatic[2] = q2e.joint(qJointStatic, listOf(-1, -1, -1))[1].toDouble()
                }
                "l_hip" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[4]), segQ90[0])
                    rotStatic[3] = q2e.joint(qJointStatic, listOf(1, 1, -1))[1].toDouble()
                }
                "r_knee" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[5]), segQ90[3])
                    rotStatic[4] = q2e.joint(qJointStatic, listOf(-1, -1, 1))[1].toDouble()
                }
                "l_knee" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[6]), segQ90[4])
                    rotStatic[5] = q2e.joint(qJointStatic, listOf(1, 1, 1))[1].toDouble()
                }
                "r_ankle" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[7]), segQ90[5])
                    rotStatic[6] = q2e.joint(qJointStatic, listOf(-1, -1, -1))[1].toDouble()
                }
                "l_ankle" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[8]), segQ90[6])
                    rotStatic[7] = q2e.joint(qJointStatic, listOf(1, 1, -1))[1].toDouble()
                }
                "r_shoulder" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[9]), segQ90[1])
                    rotStatic[8] = q2e.joint(qJointStatic, listOf(-1, -1, -1))[1].toDouble()
                }
                "l_shoulder" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[10]), segQ90[1])
                    rotStatic[9] = q2e.joint(qJointStatic, listOf(1, 1, -1))[1].toDouble()
                }
                "r_elbow" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[11]), segQ90[9])
                    rotStatic[10] = q2e.joint(qJointStatic, listOf(-1, -1, -1))[1].toDouble()
                }
                "l_elbow" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[12]), segQ90[10])
                    rotStatic[11] = q2e.joint(qJointStatic, listOf(1, 1, -1))[1].toDouble()
                }
                "r_wrist" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[13]), segQ90[11])
                    rotStatic[12] = q2e.joint(qJointStatic, listOf(1, 1, 1))[1].toDouble()
                }
                "l_wrist" -> {
                    val qJointStatic = operations.qProd(operations.qConj(segQ90[14]), segQ90[12])
                    rotStatic[13] = q2e.joint(qJointStatic, listOf(1, 1, 1))[1].toDouble()
                }
            }
        }
        val staticRotations: List<Double> = rotStatic
        return staticRotations
    }    
}