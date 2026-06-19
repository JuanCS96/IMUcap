package com.biomechApp.imuCap.utils

import kotlin.math.asin
import kotlin.math.atan2

class Q2E {

    fun joint(q: List<Float>, sig: List<Int>): List<Float> {
        val t0 = 2 * (q[1] * q[3] + q[2] * q[0])
        val t1 = 2 * (q[0] * q[0] + q[3] * q[3]) - 1
        val rot = (Math.toDegrees(atan2(t0.toDouble(), t1.toDouble()))).toFloat()

        var t2 = 2 * (q[2] * q[3] - q[1] * q[0])
        t2 = if (t2 > 1) 1F else t2
        t2 = if (t2 < -1) -1F else t2
        val abd = (Math.toDegrees(asin(t2.toDouble()))).toFloat()

        val t3 = 2 * (q[1] * q[2] + q[3] * q[0])
        val t4 = 2 * (q[0] * q[0] + q[2] * q[2]) - 1
        val flex = (Math.toDegrees(atan2(t3.toDouble(), t4.toDouble()))).toFloat()

        return listOf(abd*sig[0], rot*sig[1], flex*sig[2])
    }

    fun segment(q: List<Float>): List<Float>{
        val t0 = 2 * (q[2] * q[3] + q[0] * q[1])
        val t1 = 2 * (q[0] * q[0] + q[3] * q[3])-1
        val xRot = (Math.toDegrees(atan2(t0.toDouble(), t1.toDouble()))).toFloat()

        var t2 = 2 * (q[1] * q[3] - q[0] * q[2])
        t2 = if (t2 > 1) 1F else t2
        t2 = if (t2 < -1) -1F else t2
        val yRot = (Math.toDegrees(-asin(t2.toDouble()))).toFloat()

        val t3 = 2 * (q[1] * q[2] + q[0] * q[3])
        val t4 = 2 * (q[0] * q[0] + q[1] * q[1])-1
        val zRot = (Math.toDegrees(atan2(t3.toDouble(), t4.toDouble()))).toFloat()

        return listOf(xRot, yRot, zRot)
    }
}