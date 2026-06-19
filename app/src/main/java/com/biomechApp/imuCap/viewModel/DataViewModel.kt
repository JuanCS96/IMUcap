package com.biomechApp.imuCap.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biomechApp.imuCap.utils.Names
import kotlin.math.roundToInt

enum class Joint {
    neck, lumbar, rHip, lHip, rKnee, lKnee, rAnkle, lAnkle,
    rShoulder, lShoulder, rElbow, lElbow, rWrist, lWrist
}

enum class JointDoF {
    Abd, Rot, Flex
}

enum class Segment {
    pelvis, torso, head,
    rThigh, lThigh, rShank, lShank, rFoot, lFoot,
    rArm, lArm, rForearm, lForearm, rHand, lHand
}

enum class SegmentDoF {
    X, Y, Z
}

data class JointSignal(
    val values: SnapshotStateList<Float> = mutableStateListOf(),
    val pathLiveData: MutableLiveData<Path> = MutableLiveData(Path())
)

data class SegmentSignal(
    val values: SnapshotStateList<Float> = mutableStateListOf(),
    val pathLiveData: MutableLiveData<Path> = MutableLiveData(Path())
)

class DataViewModel: ViewModel() {

    val jointSignals: MutableMap<Pair<Joint, JointDoF>, JointSignal> = mutableMapOf()
    val segmentSignals: MutableMap<Pair<Segment, SegmentDoF>, SegmentSignal> = mutableMapOf()

    init {
        for (joint in Joint.values()) {
            for (dof in JointDoF.values()) {
                jointSignals[joint to dof] = JointSignal()
            }
        }
        for (segment in Segment.values()) {
            for (dof in SegmentDoF.values()) {
                segmentSignals[segment to dof] = SegmentSignal()
            }
        }
    }

    fun getJointValues(joint: Joint, dof: JointDoF): SnapshotStateList<Float> {
        return jointSignals[joint to dof]?.values ?: mutableStateListOf()
    }
    fun getSegmentValues(segment: Segment, dof: SegmentDoF): SnapshotStateList<Float> {
        return segmentSignals[segment to dof]?.values ?: mutableStateListOf()
    }

    fun setJointValues(joint: Joint, dof: JointDoF, value: Float) {
        if (jointSignals[joint to dof]?.values?.size!! < 300) {
            jointSignals[joint to dof]?.values?.add(value)
        }
        else {
            jointSignals[joint to dof]?.values?.removeAt(0)
            jointSignals[joint to dof]?.values?.add(value)
        }
    }
    fun setSegmentValues(segment: Segment, dof: SegmentDoF, value: Float) {
        if (segmentSignals[segment to dof]?.values?.size!! < 300) {
            segmentSignals[segment to dof]?.values?.add(value)
        }
        else {
            segmentSignals[segment to dof]?.values?.removeAt(0)
            segmentSignals[segment to dof]?.values?.add(value)
        }
    }

    fun getJointPathLiveData(joint: Joint, dof: JointDoF): LiveData<Path> {
        return jointSignals[joint to dof]?.pathLiveData ?: MutableLiveData(Path())
    }
    fun getSegmentPathLiveData(segment: Segment, dof: SegmentDoF): LiveData<Path> {
        return segmentSignals[segment to dof]?.pathLiveData ?: MutableLiveData(Path())
    }


    fun updatePaths() {

        val dataMap: MutableList<SnapshotStateList<Float>> = mutableListOf()
        for (i in 0 until idxDataSelected.size) {
            if (idxDataSelected[i][2] == -1) {
                dataMap.add(mutableStateListOf())
            }
            else if (idxDataSelected[i][2] == 0) {
                val pair = idxDataSelected[i]
                val joint = Joint.values()[pair[0]]
                val dof = JointDoF.values()[pair[1]]
                dataMap.add(jointSignals[joint to dof]?.values!!)
            }
            else if (idxDataSelected[i][2] == 1) {
                val pair = idxDataSelected[i]
                val segment = Segment.values()[pair[0]]
                val dof = SegmentDoF.values()[pair[1]]
                dataMap.add(segmentSignals[segment to dof]?.values!!)
            }
        }

        val maxValue = dataMap.mapNotNull { it.maxOrNull() }.maxOrNull()
        val minValue = dataMap.mapNotNull { it.minOrNull() }.minOrNull()

        for (i in 0 until idxDataSelected.size) {
            if ((idxDataSelected[i][2] != -1 && dataMap[i].isNotEmpty()) &&
                (maxValue != null && minValue != null)) {

                val values = dataMap[i]

                val dataPlot = mutableListOf<Float>()
                val newPath = Path()

                for (value in values) {
                    val mappedValue = (value - minValue) / (maxValue - minValue) * (screenWidth - ((screenWidth / 7) / 2))
                    dataPlot.add(mappedValue)
                }

                val increment = screenHeight/dataPlot.size
                var count = 0f
                newPath.moveTo(dataPlot[0], 0f)
                for (i in 1 until dataPlot.size){
                    count += increment
                    newPath.lineTo(dataPlot[i], count)
                }
                if (idxDataSelected[i][2] == 0) {
                    val pair = idxDataSelected[i]
                    val joint = Joint.values()[pair[0]]
                    val dof = JointDoF.values()[pair[1]]
                    jointSignals[joint to dof]?.pathLiveData?.postValue(newPath)
                }
                else {
                    val pair = idxDataSelected[i]
                    val segment = Segment.values()[pair[0]]
                    val dof = SegmentDoF.values()[pair[1]]
                    segmentSignals[segment to dof]?.pathLiveData?.postValue(newPath)
                }
            }
        }
        if (maxValue != null && minValue != null) {
            axis(maxValue, minValue)
        }
        else {
            axis(60f, 0f)
        }
    }


    private val jointDoF = Names().jointsDoF()
    private val segmentDoF = Names().segmentsDoF()

    val idxDataSelected = MutableList(4) { List(3) { -1 } }
    private val _dataSelected = MutableLiveData<List<String>>(List(4) { "None" })
    val dataSelected: LiveData<List<String>> = _dataSelected
    fun onDataSelectedChange(data: String, idx: Int) {
        val currentList = _dataSelected.value?.toMutableList() ?: MutableList(4) { "None" }
        currentList[idx] = data
        _dataSelected.value = currentList.toList()

        if (currentList.all { it == "None" }){
            for (i in 0 until idxDataSelected.size) {
                idxDataSelected[i] = listOf(-1, -1, -1)
            }
        }
        else{
            for (i in 0 until currentList.size) {
                if (currentList[i] == "None") {
                    idxDataSelected[i] = listOf(-1, -1, -1)
                }
                else if (jointDoF.any { sublist -> currentList[i] in sublist }) {
                    loop@ for (joint in jointDoF.indices) {
                        for (dof in jointDoF[joint].indices) {
                            if (currentList[i] == jointDoF[joint][dof]) {
                                idxDataSelected[i] = listOf(joint, dof, 0)
                                break@loop
                            }
                        }
                    }
                }
                else if (segmentDoF.any { sublist -> currentList[i] in sublist }) {
                    loop@ for (segment in segmentDoF.indices) {
                        for (dof in segmentDoF[segment].indices) {
                            if (currentList[i] == segmentDoF[segment][dof]) {
                                idxDataSelected[i] = listOf(segment, dof, 1)
                                break@loop
                            }
                        }
                    }
                }
            }
        }
    }

    var screenWidth: Float = 0f

    fun onScreenWidthChange(screenWidth:Float){
        this.screenWidth = screenWidth
    }

    var screenHeight: Float = 0f

    fun onScreenHeightChange(screenHeight:Float){
        this.screenHeight = screenHeight
    }

    private val legendSelected: MutableList<String> = MutableList(4) {"None"}
    fun onLegendSelectedChange(legend:String, idx: Int){
        legendSelected[idx] = legend
    }

    private val _axes = MutableLiveData<List<String>>()
    val axes: LiveData<List<String>> = _axes
    private fun axis(maxValue: Float, minValue: Float) {
        val step = (maxValue - minValue) / 6
        val scaleValues = mutableListOf<String>()
        for (i in 0 until 7){
            scaleValues.add((((minValue + i * step) * 10).roundToInt() / 10f).toString())
        }
        _axes.postValue(scaleValues.toList())
    }

    fun clearData(){
        for (i in Joint.values()) {
            for (j in JointDoF.values()) {
                jointSignals[i to j]?.values?.clear()
                jointSignals[i to j]?.pathLiveData?.postValue(Path())
            }
        }
        for (i in Segment.values()) {
            for (j in SegmentDoF.values()) {
                segmentSignals[i to j]?.values?.clear()
                segmentSignals[i to j]?.pathLiveData?.postValue(Path())
            }
        }
        axis(60f, 0f)
    }
}