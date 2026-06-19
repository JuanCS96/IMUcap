package com.biomechApp.imuCap.model

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.biomechApp.imuCap.utils.Filter
import com.biomechApp.imuCap.utils.Operations
import com.biomechApp.imuCap.utils.Q2E
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.Joint
import com.biomechApp.imuCap.viewModel.JointDoF
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.Segment
import com.biomechApp.imuCap.viewModel.SegmentDoF
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Streaming(
    val sensorListViewModel: SensorListViewModel,
    val dataViewModel: DataViewModel,
    private val recordingViewModel: RecordingViewModel,
    private val avatarViewModel: AvatarViewModel,
    val appContext: Context,
    private val dataWriter: Writer
) {

    private val operations = Operations()
    private val q2e = Q2E()
    private val filterIni = Filter()

    private val rotStatic = filterIni.zero(sensorListViewModel).map { it.toFloat() }
    val S2B = MutableList(15) { listOf<Float>() }

    init {
        for (i in 0 until sensorListViewModel.staticSegments.size) {
            val static = sensorListViewModel.staticSegments[i]
            val local = sensorListViewModel.localFrames[i]
            if (static.isNotEmpty() && local.isNotEmpty()) {
                S2B[i] = operations.qProd(operations.qConj(local), static)
            }
        }

        sensorListViewModel.onCleanBuffer()
        dataViewModel.clearData()
        sensorListViewModel.onRecordingTypeChange("stream")
    }

    fun streamingOn(lifecycleOwner: LifecycleOwner){
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            while (sensorListViewModel.recordingState){
                if (sensorListViewModel.buffers.filter { it.isNotEmpty() }.all { it.size > 10 }) {

                    try {

                        val segQ90 = MutableList(15) { listOf<Float>() }
                        val segQ = MutableList(15) { listOf<Float>() }
                        for (i in 0 until sensorListViewModel.buffers.size) {
                            if (sensorListViewModel.buffers[i].isNotEmpty()) {
                                val sensorQ = (sensorListViewModel.buffers[i][0].quat).toList()
                                val segmentQ = operations.qProd(sensorQ, operations.qConj(S2B[i]))
                                segQ[i] = segmentQ
                                segQ90[i] = operations.rot90x(segmentQ)
                            }
                        }

                        val jointAngles = MutableList(14) { listOf<Float>() }
                        for (joint in sensorListViewModel.jointSelected) {
                            when (joint){
                                "neck" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[2]), segQ90[1])
                                    jointAngles[0] = q2e.joint(qJoint, listOf(1, -1, 1))
                                        .run { listOf(get(0), get(1)-rotStatic[0], get(2)) }
                                    dataViewModel.setJointValues(Joint.neck, JointDoF.Abd, jointAngles[0][0])
                                    dataViewModel.setJointValues(Joint.neck, JointDoF.Rot, jointAngles[0][1])
                                    dataViewModel.setJointValues(Joint.neck, JointDoF.Flex, jointAngles[0][2])
                                }
                                "lumbar" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[1]), segQ90[0])
                                    jointAngles[1] = q2e.joint(qJoint, listOf(1, -1, 1))
                                        .run { listOf(get(0), get(1)-rotStatic[1], get(2)) }
                                    dataViewModel.setJointValues(Joint.lumbar, JointDoF.Abd, jointAngles[1][0])
                                    dataViewModel.setJointValues(Joint.lumbar, JointDoF.Rot, jointAngles[1][1])
                                    dataViewModel.setJointValues(Joint.lumbar, JointDoF.Flex, jointAngles[1][2])
                                }
                                "r_hip" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[3]), segQ90[0])
                                    jointAngles[2] = q2e.joint(qJoint, listOf(-1, -1, -1))
                                    .run { listOf(get(0), get(1)-rotStatic[2], get(2)) }
                                    dataViewModel.setJointValues(Joint.rHip, JointDoF.Abd, jointAngles[2][0])
                                    dataViewModel.setJointValues(Joint.rHip, JointDoF.Rot, jointAngles[2][1])
                                    dataViewModel.setJointValues(Joint.rHip, JointDoF.Flex, jointAngles[2][2])
                                }
                                "l_hip" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[4]), segQ90[0])
                                    jointAngles[3] = q2e.joint(qJoint, listOf(1, 1, -1))
                                    .run { listOf(get(0), get(1)-rotStatic[3], get(2)) }
                                    dataViewModel.setJointValues(Joint.lHip, JointDoF.Abd, jointAngles[3][0])
                                    dataViewModel.setJointValues(Joint.lHip, JointDoF.Rot, jointAngles[3][1])
                                    dataViewModel.setJointValues(Joint.lHip, JointDoF.Flex, jointAngles[3][2])
                                }
                                "r_knee" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[5]), segQ90[3])
                                    jointAngles[4] = q2e.joint(qJoint, listOf(-1, -1, 1))
                                    .run { listOf(get(0)*0.25f, (get(1)-rotStatic[4])*0.5f, get(2)) }
                                    dataViewModel.setJointValues(Joint.rKnee, JointDoF.Abd, jointAngles[4][0])
                                    dataViewModel.setJointValues(Joint.rKnee, JointDoF.Rot, jointAngles[4][1])
                                    dataViewModel.setJointValues(Joint.rKnee, JointDoF.Flex, jointAngles[4][2])
                                }
                                "l_knee" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[6]), segQ90[4])
                                    jointAngles[5] = q2e.joint(qJoint, listOf(1, 1, 1))
                                    .run { listOf(get(0)*0.25f, (get(1)-rotStatic[5])*0.5f, get(2)) }
                                    dataViewModel.setJointValues(Joint.lKnee, JointDoF.Abd, jointAngles[5][0])
                                    dataViewModel.setJointValues(Joint.lKnee, JointDoF.Rot, jointAngles[5][1])
                                    dataViewModel.setJointValues(Joint.lKnee, JointDoF.Flex, jointAngles[5][2])
                                }
                                "r_ankle" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[7]), segQ90[5])
                                    jointAngles[6] = q2e.joint(qJoint, listOf(-1, -1, -1))
                                    .run { listOf(get(0), get(1)-rotStatic[6], get(2)) }
                                    dataViewModel.setJointValues(Joint.rAnkle, JointDoF.Abd, jointAngles[6][0])
                                    dataViewModel.setJointValues(Joint.rAnkle, JointDoF.Rot, jointAngles[6][1])
                                    dataViewModel.setJointValues(Joint.rAnkle, JointDoF.Flex, jointAngles[6][2])
                                }
                                "l_ankle" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[8]), segQ90[6])
                                    jointAngles[7] = q2e.joint(qJoint, listOf(1, 1, -1))
                                    .run { listOf(get(0), get(1)-rotStatic[7], get(2)) }
                                    dataViewModel.setJointValues(Joint.lAnkle, JointDoF.Abd, jointAngles[7][0])
                                    dataViewModel.setJointValues(Joint.lAnkle, JointDoF.Rot, jointAngles[7][1])
                                    dataViewModel.setJointValues(Joint.lAnkle, JointDoF.Flex, jointAngles[7][2])
                                }
                                "r_shoulder" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[9]), segQ90[1])
                                    jointAngles[8] = q2e.joint(qJoint, listOf(-1, -1, -1))
                                        .run { listOf(get(0), get(1)-rotStatic[8], get(2)) }
                                    dataViewModel.setJointValues(Joint.rShoulder, JointDoF.Abd, jointAngles[8][0])
                                    dataViewModel.setJointValues(Joint.rShoulder, JointDoF.Rot, jointAngles[8][1])
                                    dataViewModel.setJointValues(Joint.rShoulder, JointDoF.Flex, jointAngles[8][2])
                                }
                                "l_shoulder" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[10]), segQ90[1])
                                    jointAngles[9] = q2e.joint(qJoint, listOf(1, 1, -1))
                                        .run { listOf(get(0), get(1)-rotStatic[9], get(2)) }
                                    dataViewModel.setJointValues(Joint.lShoulder, JointDoF.Abd, jointAngles[9][0])
                                    dataViewModel.setJointValues(Joint.lShoulder, JointDoF.Rot, jointAngles[9][1])
                                    dataViewModel.setJointValues(Joint.lShoulder, JointDoF.Flex, jointAngles[9][2])
                                }
                                "r_elbow" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[11]), segQ90[9])
                                    jointAngles[10] = q2e.joint(qJoint, listOf(-1, -1, -1))
                                        .run { listOf(get(0)*0.25f, get(1)-rotStatic[10], get(2)) }
                                    dataViewModel.setJointValues(Joint.rElbow, JointDoF.Abd, jointAngles[10][0])
                                    dataViewModel.setJointValues(Joint.rElbow, JointDoF.Rot, jointAngles[10][1])
                                    dataViewModel.setJointValues(Joint.rElbow, JointDoF.Flex, jointAngles[10][2])
                                }
                                "l_elbow" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[12]), segQ90[10])
                                    jointAngles[11] = q2e.joint(qJoint, listOf(1, 1, -1))
                                        .run { listOf(get(0)*0.25f, get(1)-rotStatic[11], get(2)) }
                                    dataViewModel.setJointValues(Joint.lElbow, JointDoF.Abd, jointAngles[11][0])
                                    dataViewModel.setJointValues(Joint.lElbow, JointDoF.Rot, jointAngles[11][1])
                                    dataViewModel.setJointValues(Joint.lElbow, JointDoF.Flex, jointAngles[11][2])
                                }
                                "r_wrist" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[13]), segQ90[11])
                                    jointAngles[12] = q2e.joint(qJoint, listOf(1, 1, 1))
                                        .run { listOf(get(2), (get(1)-rotStatic[12])*0.25f, get(0)) }
                                    dataViewModel.setJointValues(Joint.rWrist, JointDoF.Abd, jointAngles[12][0])
                                    dataViewModel.setJointValues(Joint.rWrist, JointDoF.Rot, jointAngles[12][1])
                                    dataViewModel.setJointValues(Joint.rWrist, JointDoF.Flex, jointAngles[12][2])
                                }
                                "l_wrist" -> {
                                    val qJoint = operations.qProd(operations.qConj(segQ90[14]), segQ90[12])
                                    jointAngles[13] = q2e.joint(qJoint, listOf(1, 1, 1))
                                        .run { listOf(get(2), (get(1)-rotStatic[13])*0.25f, get(0)) }
                                    dataViewModel.setJointValues(Joint.lWrist, JointDoF.Abd, jointAngles[13][0])
                                    dataViewModel.setJointValues(Joint.lWrist, JointDoF.Rot, jointAngles[13][1])
                                    dataViewModel.setJointValues(Joint.lWrist, JointDoF.Flex, jointAngles[13][2])
                                }
                            }
                        }

                        dataViewModel.updatePaths()

                        val data2write = mutableStateListOf<Float>()

                        for (i in jointAngles.indices) {
                            if (jointAngles[i].isNotEmpty()) {
                                for (j in jointAngles[i].indices) {
                                    data2write.add(jointAngles[i][j])
                                }
                            }
                            else {
                                data2write.add(0F)
                                data2write.add(0F)
                                data2write.add(0F)
                            }
                        }

                        val segmentsOri = MutableList(15) { listOf<Float>() }
                        for (i in 0 until sensorListViewModel.segmentSelected.size) {
                            if (sensorListViewModel.segmentSelected[i] != "None") {
                                segmentsOri[i] = q2e.segment(segQ[i])
                                    .run { listOf(get(0), get(1), get(2)) }
                            }
                        }

                        for (i in segmentsOri.indices) {
                            if (segmentsOri[i].isNotEmpty()) {
                                val segment = Segment.values()[i]
                                for (j in segmentsOri[i].indices) {
                                    val segmentDof = SegmentDoF.values()[j]
                                    dataViewModel.setSegmentValues(segment, segmentDof, segmentsOri[i][j])
                                    data2write.add(segmentsOri[i][j])
                                }
                            }
                            else {
                                data2write.add(0F)
                                data2write.add(0F)
                                data2write.add(0F)
                            }
                        }

                        if (recordingViewModel.startWriting.value == true){
                            dataWriter.writeData(data2write)
                        }

                        if ((listOf(2, 3, 4, 5, 6, 7).none { jointAngles[it].isEmpty() }) &&
                            (avatarViewModel.unityStart.value == true)) {
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[1, ${segmentsOri[0][1]}, ${segmentsOri[0][2]}, ${segmentsOri[0][0]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[2, ${-jointAngles[2][2]}, ${-jointAngles[2][0]}, ${jointAngles[2][1]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[3, ${-jointAngles[3][2]}, ${jointAngles[3][0]}, ${-jointAngles[3][1]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[4, ${jointAngles[4][2]}, ${jointAngles[4][0]}, ${jointAngles[4][1]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[5, ${jointAngles[5][2]}, ${-jointAngles[5][0]}, ${-jointAngles[5][1]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[6, ${-jointAngles[6][2]}, ${jointAngles[6][1]}, ${jointAngles[6][0]}]")
                            UnityPlayer.UnitySendMessage(
                                "pocketMocapModel", "ModelData", "[7, ${-jointAngles[7][2]}, ${-jointAngles[7][1]}, ${-jointAngles[7][0]}]")
                        }

                        for (buffer in sensorListViewModel.buffers) {
                            if (buffer.isNotEmpty()) {
                                buffer.removeAt(0)
                            }
                        }

                    }catch (e: Exception){
                        for (buffer in sensorListViewModel.buffers) {
                            if (buffer.isNotEmpty()) {
                                buffer.removeAt(0)
                            }
                        }
                        continue
                    }
                }
            }
        }
    }
}
