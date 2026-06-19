package com.biomechApp.imuCap.utils


class TimeRanges {

    fun run (
        segmentCount: Int,
        segmentNames: List<String>,
        outputRate: Int,
        dataLen: Int
    ): MutableList<List<Int>> {

        val frames = dataLen / segmentCount

        val ranges: MutableList<List<Int>> = mutableListOf()
        for (i in 0 until segmentCount) {
            val ini = (frames * i) + 1
            val end = (frames * (i + 1)) - 1
            ranges.add(listOf(ini, end))
        }

        val segmentRanges = MutableList(15) { listOf<Int>() }
        for (i in segmentNames.indices) {
            when (segmentNames[i]) {
                "torso" -> {
                    segmentRanges[1] = ranges[i]
                }
                "head" -> {
                    segmentRanges[2] = ranges[i]
                }
                "right thigh" -> {
                    segmentRanges[3] = ranges[i]
                }
                "left thigh" -> {
                    segmentRanges[4] = ranges[i]
                }
                "right shank" -> {
                    segmentRanges[5] = ranges[i]
                }
                "left shank" -> {
                    segmentRanges[6] = ranges[i]
                }
                "right foot" -> {
                    segmentRanges[7] = ranges[i]
                }
                "left foot" -> {
                    segmentRanges[8] = ranges[i]
                }
                "right arm" -> {
                    segmentRanges[9] = ranges[i]
                }
                "left arm" -> {
                    segmentRanges[10] = ranges[i]
                }
                "right forearm" -> {
                    segmentRanges[11] = ranges[i]
                    segmentRanges[13] = ranges[i]
                }
                "left forearm" -> {
                    segmentRanges[12] = ranges[i]
                    segmentRanges[14] = ranges[i]
                }
            }
        }
        return segmentRanges
    }
}