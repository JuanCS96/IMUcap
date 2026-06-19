package com.biomechApp.imuCap.utils

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Operations {
    fun qToVect(q: FloatArray): List<List<Float>> {
        val r11p = 1 - 2 * (q[2].pow(2)) - 2 * (q[3].pow(2))
        val r12p = 2 * q[1] * q[2] + 2 * q[3] * q[0]
        val r13p = 2 * q[1] * q[3] - 2 * q[2] * q[0]
        val r21p = 2 * q[1] * q[2] - 2 * q[3] * q[0]
        val r22p = 1 - 2 * (q[1].pow(2)) - 2 * (q[3].pow(2))
        val r23p = 2 * q[2] * q[3] + 2 * q[1] * q[0]
        val r31p = 2 * q[1] * q[3] + 2 * q[2] * q[0]
        val r32p = 2 * q[2] * q[3] - 2 * q[1] * q[0]
        val r33p = 1 - 2 * (q[1].pow(2)) - 2 * (q[2].pow(2))
        val xVect = listOf(r11p, r12p, r13p)
        val yVect = listOf(r21p, r22p, r23p)
        val zVect = listOf(r31p, r32p, r33p)
        return listOf(xVect, yVect, zVect)
    }

    fun qConj(q: List<Float>): List<Float>{
        return listOf(q[0], -q[1], -q[2], -q[3])
    }

    fun qProd(q1: List<Float>, q2: List<Float>): List<Float> {
        val qw = q1[0] * q2[0] - q1[1] * q2[1] - q1[2] * q2[2] - q1[3] * q2[3]
        val qx = q1[0] * q2[1] + q1[1] * q2[0] + q1[2] * q2[3] - q1[3] * q2[2]
        val qy = q1[0] * q2[2] - q1[1] * q2[3] + q1[2] * q2[0] + q1[3] * q2[1]
        val qz = q1[0] * q2[3] + q1[1] * q2[2] - q1[2] * q2[1] + q1[3] * q2[0]
        return listOf(qw, qx, qy, qz)
    }

    fun angVect(v1: List<Float>, v2: List<Float>): Float {
        val sp = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2]
        val spAbs = sqrt(v1[0].pow(2) + v1[1].pow(2) + v1[2].pow(2)) *
                sqrt(v2[0].pow(2) + v2[1].pow(2) + v2[2].pow(2))
        return Math.toDegrees(acos((sp / spAbs).toDouble())).toFloat()
    }

    fun vectUni(v: List<Float>): List<Float> {
        val mod = sqrt(v[0].pow(2) + v[1].pow(2) + v[2].pow(2))
        return listOf(v[0] / mod, v[1] / mod, v[2] / mod)
    }

    fun crossProd(v1: List<Float>, v2: List<Float>): List<Float> {
        val i = v1[1] * v2[2] - v1[2] * v2[1]
        val j = (v1[0] * v2[2] - v1[2] * v2[0]) * -1
        val k = v1[0] * v2[1] - v1[1] * v2[0]
        return listOf(i, j, k)
    }

    fun rot90x(q: List<Float>): List<Float> {
        val axisX = qToVect(q.toFloatArray())[0]
        val axisRot90x = listOf(
            (cos(Math.toRadians(90.0) / 2)).toFloat(),
            (axisX[0] * sin(Math.toRadians(90.0) / 2)).toFloat(),
            (axisX[1] * sin(Math.toRadians(90.0) / 2)).toFloat(),
            (axisX[2] * sin(Math.toRadians(90.0) / 2)).toFloat()
        )
        return qProd(axisRot90x, q)
    }

    fun rot90z(q: List<Float>): List<Float> {
        val axisZ = qToVect(q.toFloatArray())[2]
        val axisRot90z = listOf(
            (cos(Math.toRadians(90.0) / 2)).toFloat(),
            (axisZ[0] * sin(Math.toRadians(90.0) / 2)).toFloat(),
            (axisZ[1] * sin(Math.toRadians(90.0) / 2)).toFloat(),
            (axisZ[2] * sin(Math.toRadians(90.0) / 2)).toFloat()
        )
        return qProd(axisRot90z, q)
    }

    fun rotJoint(q: List<Float>, ang: Double): List<Float> {
        val axisY = qToVect(q.toFloatArray())[1]
        val axisRot = listOf(
            (cos(Math.toRadians(ang) / 2)).toFloat(),
            (axisY[0] * sin(Math.toRadians(ang) / 2)).toFloat(),
            (axisY[1] * sin(Math.toRadians(ang) / 2)).toFloat(),
            (axisY[2] * sin(Math.toRadians(ang) / 2)).toFloat()
        )
        return qProd(axisRot, q)
    }


    fun rotationMatrixToQuaternion(
        r00: Float, r01: Float, r02: Float,
        r10: Float, r11: Float, r12: Float,
        r20: Float, r21: Float, r22: Float
    ): FloatArray {
        val trace = r00 + r11 + r22
        val q = FloatArray(4)

        if (trace > 0f) {
            val s = 0.5f / sqrt(trace + 1.0f)
            q[0] = 0.25f / s
            q[1] = (r21 - r12) * s
            q[2] = (r02 - r20) * s
            q[3] = (r10 - r01) * s
        } else if ((r00 > r11) && (r00 > r22)) {
            val s = 2.0f * sqrt(1.0f + r00 - r11 - r22)
            q[0] = (r21 - r12) / s
            q[1] = 0.25f * s
            q[2] = (r01 + r10) / s
            q[3] = (r02 + r20) / s
        } else if (r11 > r22) {
            val s = 2.0f * sqrt(1.0f + r11 - r00 - r22)
            q[0] = (r02 - r20) / s
            q[1] = (r01 + r10) / s
            q[2] = 0.25f * s
            q[3] = (r12 + r21) / s
        } else {
            val s = 2.0f * sqrt(1.0f + r22 - r00 - r11)
            q[0] = (r10 - r01) / s
            q[1] = (r02 + r20) / s
            q[2] = (r12 + r21) / s
            q[3] = 0.25f * s
        }
        return q
    }
}