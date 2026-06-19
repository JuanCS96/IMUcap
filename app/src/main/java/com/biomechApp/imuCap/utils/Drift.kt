package com.biomechApp.imuCap.utils

import com.xsens.dot.android.sdk.events.DotData

class Drift {
    fun checkDrift(vector: List<DotData>, rate: Int): Boolean {
        val quatW: MutableList<Float> = mutableListOf()
        val quatX: MutableList<Float> = mutableListOf()
        val quatY: MutableList<Float> = mutableListOf()
        val quatZ: MutableList<Float> = mutableListOf()
        for (i in (vector.size*0.75).toInt() until vector.size) {
            quatW.add(vector[i].quat[0])
            quatX.add(vector[i].quat[1])
            quatY.add(vector[i].quat[2])
            quatZ.add(vector[i].quat[3])
        }
        val difW = quatW.max() - quatW.min() > 0.015
        val difX = quatX.max() - quatX.min() > 0.015
        val difY = quatY.max() - quatY.min() > 0.015
        val difZ = quatZ.max() - quatZ.min() > 0.015

        return !(!difW && !difX && !difY && !difZ)
    }
}