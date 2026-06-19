package com.biomechApp.imuCap.utils


class KeyPoints {

    fun run(
        gyrAbs: List<MutableList<Double>>,
        index: Int,
        rate: Int,
        segmentRanges: MutableList<List<Int>>
    ): Int {

        val gyrAbsData: MutableList<Double> = gyrAbs[index]
        val segmentRange = segmentRanges[index]
        val threshold = 40

        val instants: MutableList<MutableList<Int>> = mutableListOf()
        val instant: MutableList<Int> = mutableListOf()
        for (i in segmentRange[0] until segmentRange[1]) {
            if (gyrAbsData[i] >= threshold && gyrAbsData[i - 1] < threshold) {
                instant.add(i)
            }
            if ((instant.size == 1) && (gyrAbsData[i] >= threshold && gyrAbsData[i + 1] < threshold)) {
                instant.add(i + 1)
            }
            if (instant.size == 2) {
                instants.add(instant.toMutableList())
                break
            }
        }

        var outputInstant = 0
        for (i in 0 until instants.size) {
            val gyrSensorAbsSub = gyrAbsData.subList(instants[i][0], instants[i][1])
            val gyrSensorAbsMax = gyrSensorAbsSub.max()
            val gyrSensorAbsMaxInst: Int = gyrSensorAbsSub.indexOf(gyrSensorAbsMax)
            outputInstant = instants[i][0] + gyrSensorAbsMaxInst
        }
        return outputInstant
    }
}
