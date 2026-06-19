package com.biomechApp.imuCap.model

import android.content.Context
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Writer(private val context: Context,
             private val configViewModel: ConfigViewModel,
             private val recordingViewModel: RecordingViewModel,
) {
    private val recordingsFolder = "recordings"
    private var csvFileNameOutput: String = ""
    private val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    private val csvHeaders = arrayOf(
        "Neck Right(+)/Left(-) Lateral Bending", "Neck Axial Rotation", "Neck Flexion(+)/Extension(-)",
        "Lumbar Right(+)/Left(-) Lateral Bending", "Lumbar Axial Rotation", "Lumbar Flexion(+)/Extension(-)",
        "Right Hip Abduction(+)/Adduction(-)", "Right Hip Internal(+)/External(-) Rotation", "Right Hip Flexion(+)/Extension(-)",
        "Left Hip Abduction(+)/Adduction(-)", "Left Hip Internal(+)/External(-) Rotation", "Left Hip Flexion(+)/Extension(-)",
        "Right Knee Abduction(+)/Adduction(-)", "Right Knee Internal(+)/External(-) Rotation", "Right Knee Flexion(+)/Extension(-)",
        "Left Knee Abduction(+)/Adduction(-)", "Left Knee Internal(+)/External(-) Rotation", "Left Knee Flexion(+)/Extension(-)",
        "Right Ankle Abduction(+)/Adduction(-)", "Right Ankle Internal(+)/External(-) Rotation", "Right Ankle Dorsiflexion/Plantarflexion",
        "Left Ankle Abduction(+)/Adduction(-)", "Left Ankle Internal(+)/External(-) Rotation", "Left Ankle Dorsiflexion/Plantarflexion",
        "Right Shoulder Abduction(+)/Adduction(-)", "Right Shoulder Internal(+)/External(-) Rotation", "Right Shoulder Flexion(+)/Extension(-)",
        "Left Shoulder Abduction(+)/Adduction(-)", "Left Shoulder Internal(+)/External(-) Rotation", "Left Shoulder Flexion(+)/Extension(-)",
        "Right Elbow Radial(+)/Ulnar(-) Deviation", "Right Elbow Pronation(+)/Supination(-)", "Right Elbow Flexion(+)/Extension(-)",
        "Left Elbow Radial(+)/Ulnar(-) Deviation", "Left Elbow Pronation(+)/Supination(-)", "Left Elbow Flexion(+)/Extension(-)",
        "Right Wrist Radial(+)/Ulnar(-) Deviation", "Right Wrist Pronation(+)/Supination(-)", "Right Wrist Flexion(+)/Extension(-)",
        "Left Wrist Radial(+)/Ulnar(-) Deviation", "Left Wrist Pronation(+)/Supination(-)", "Left Wrist Flexion(+)/Extension(-)",
        "Pelvis X", "Pelvis Y", "Pelvis Z",
        "Torso X", "Torso Y", "Torso Z",
        "Head X", "Head Y", "Head Z",
        "Right Thigh X", "Right Thigh Y", "Right Thigh Z",
        "Left Thigh X", "Left Thigh Y", "Left Thigh Z",
        "Right Shank X", "Right Shank Y", "Right Shank Z",
        "Left Shank X", "Left Shank Y", "Left Shank Z",
        "Right Foot X", "Right Foot Y", "Right Foot Z",
        "Left Foot X", "Left Foot Y", "Left Foot Z",
        "Right Arm X", "Right Arm Y", "Right Arm Z",
        "Left Arm X", "Left Arm Y", "Left Arm Z",
        "Right Forearm X", "Right Forearm Y", "Right Forearm Z",
        "Left Forearm X", "Left Forearm Y", "Left Forearm Z",
        "Right Hand X", "Right Hand Y", "Right Hand Z",
        "Left Hand X", "Left Hand Y", "Left Hand Z"
    )

    init {
        val csvDirectory = File(context.getExternalFilesDir(null), recordingsFolder)
        if (!csvDirectory.exists()) {
            csvDirectory.mkdirs()
        }
    }

    private fun getCsvName(): String {
        val csvFileName = configViewModel.fileOutName
        val currentTime = dateFormat.format(Date())
        return csvFileName +"_"+ currentTime +".csv"
    }

    fun createCsv() {
        csvFileNameOutput = getCsvName()
        val csvFile = File(context.getExternalFilesDir(recordingsFolder), csvFileNameOutput)

        try {
            csvFile.createNewFile()
            FileWriter(csvFile).use { writer ->
                writer.write(csvHeaders.joinToString(",") + "\n")
            recordingViewModel.onStartWritingChange(true)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeData(data: MutableList<Float>) {
        val csvFile = File(context.getExternalFilesDir(recordingsFolder), csvFileNameOutput)

        try {
            FileWriter(csvFile, true).use { writer ->
                writer.write(data.joinToString(",") + "\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}