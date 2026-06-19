package com.biomechApp.imuCap.view.second

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.model.Recording
import com.biomechApp.imuCap.model.Restart
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.utils.Timer
import com.biomechApp.imuCap.view.navegation.BottomBar
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun SecondScreen(navigationController: NavHostController,
                 indexBottom:Int,
                 configViewModel: ConfigViewModel,
                 sensorListViewModel: SensorListViewModel,
                 calibViewModel: CalibrationViewModel,
                 recordingViewModel: RecordingViewModel,
                 scannerViewModel: ScannerViewModel,
                 dataViewModel: DataViewModel,
                 avatarViewModel: AvatarViewModel,
                 scanner: Scanner,
                 lifecycleOwner: LifecycleOwner,
                 appContext: Context,
                 dataWriter: Writer
) {
    val showStartButton: Boolean by calibViewModel.showStartButton.observeAsState(initial = true)
    val showSensorCalib: Boolean by calibViewModel.showSensorCalib.observeAsState(initial = false)
    val showNoSensorCalibReady: Boolean by calibViewModel.showNoSensorCalibReady.observeAsState(initial = false)
    val showNoName: Boolean by configViewModel.showNoName.observeAsState(initial = false)
    val showJointsMismatch: Boolean by calibViewModel.showJointsMismatch.observeAsState(initial = false)
    val showSensorCalibProgress: Boolean by calibViewModel.showSensorCalibProgress.observeAsState(initial = false)
    val sensorCalibProgress: Float by calibViewModel.sensorCalibProgress.observeAsState(initial = 0.0f)
    val calibDif: Boolean by sensorListViewModel.calibResultDif.observeAsState(initial = false)
    val calibDrift: Boolean by sensorListViewModel.calibResultDrift.observeAsState(initial = false)
    val calibOk: Boolean by sensorListViewModel.calibResultOk.observeAsState(initial = false)
    val showStatic: Boolean by calibViewModel.showStatic.observeAsState(initial = false)
    val staticProgress: Float by calibViewModel.staticProgress.observeAsState(initial = 0.0f)
    val showDynamic: Boolean by calibViewModel.showDynamic.observeAsState(initial = false)
    val dynamicProgress: Float by calibViewModel.dynamicProgress.observeAsState(initial = 0.0f)
    val showStopButton: Boolean by calibViewModel.showStopButton.observeAsState(initial = false)
    val playStop: String by recordingViewModel.recordingState.observeAsState(initial = "play")
    val stopStreaming: Boolean by recordingViewModel.showStopRecording.observeAsState(initial = false)
    val showGoodCalibLevel: Boolean by calibViewModel.showGoodModelCalibLevel.observeAsState(initial = false)
    val showPoorCalibLevel: Boolean by calibViewModel.showPoorModelCalibLevel.observeAsState(initial = false)
    val showBadCalibLevel: Boolean by calibViewModel.showBadModelCalibLevel.observeAsState(initial = false)
    val selectedHz:String by configViewModel.selectedHz.observeAsState(initial = "30 Hz")
    val bendingSegment: String by calibViewModel.segmentToBend.observeAsState(initial = "")

    Scaffold(
        topBar = { SecondTopBar(configViewModel) },
        bottomBar = { BottomBar(
            navigationController,
            indexBottom, scanner,
            scannerViewModel,
            recordingViewModel
        ) }) {

        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#0071c1")))) {

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.10f)
                        .background(Color(android.graphics.Color.parseColor("#0071c1"))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Calibration", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.80f)
                        .background(Color(android.graphics.Color.parseColor("#0071c1")))
                ) {
                    JointTicks(sensorListViewModel)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.10f)
                        .background(Color(android.graphics.Color.parseColor("#0071c1"))),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (showStartButton) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            onClick = {
                                if (sensorListViewModel.onCheckJointsSelected()) {
                                    if (sensorListViewModel.recordingType == "null" &&
                                        sensorListViewModel.connectionLiveList.size >= 2 &&
                                        sensorListViewModel.jointSelected.size >= 1 &&
                                        !sensorListViewModel.checkDuplicateSegments() &&
                                        sensorListViewModel.onCheckNumSensors()) {
                                        if (configViewModel.fileOutName != ""){
                                            calibViewModel.onShowSensorCalibChange(true)
                                        }
                                        else{
                                            configViewModel.onShowNoNameChange(true)
                                        }
                                    }
                                    else {
                                        calibViewModel.onShowNoSensorCalibReadyChange(true)
                                    }
                                }
                                else {
                                    calibViewModel.onShowJointsMismatchChange(true)
                                }
                            }
                        ) {
                            Text(text = "Start", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                    if (showSensorCalibProgress) {
                        Column() {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.05f),
                                contentAlignment = Alignment.Center) {
                                Text(text = "Calibrating sensors....", color = Color.White, fontSize = 20.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.05f),
                                contentAlignment = Alignment.TopCenter) {
                                LinearProgressIndicator(
                                    progress = {sensorCalibProgress},
                                    color = Color.Green,
                                    trackColor = Color.White,
                                    gapSize = (-2).dp,
                                    drawStopIndicator = {},
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    if (showStatic) {
                        Column() {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                contentAlignment = Alignment.Center) {
                                Text(text = "Hold Pose", color = Color.White, fontSize = 20.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                contentAlignment = Alignment.TopCenter) {
                                LinearProgressIndicator(
                                    progress = { staticProgress },
                                    color = Color.Green,
                                    trackColor = Color.White,
                                    gapSize = (-2).dp,
                                    drawStopIndicator = {},
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                    if (showDynamic) {
                        Column() {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                contentAlignment = Alignment.Center) {
                                Text(text = bendingSegment, color = Color.White, fontSize = 20.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                contentAlignment = Alignment.TopCenter) {
                                LinearProgressIndicator(
                                    progress = {dynamicProgress},
                                    color = Color.Green,
                                    trackColor = Color.White,
                                    gapSize = (-2).dp,
                                    drawStopIndicator = {},
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                    if (showStopButton){
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            onClick = {
                                if (playStop == "play"){
                                    calibViewModel.onShowStopButtonChange(false)
                                    calibViewModel.onShowStartButtonChange(true)
                                    Recording(sensorListViewModel, configViewModel).stopMeasurement()
                                    sensorListViewModel.onRecordingTypeChange("null")
                                } else{
                                    recordingViewModel.onShowStopRecordingChange(true)
                                }
                            }
                        ) {
                            Text(text = "Stop", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }

    if (showNoSensorCalibReady){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowNoSensorCalibReadyChange(false) },
            title = { Text(text = "Calibrate Sensors", color = Color.White) },
            text = { Text(text = "Connect at least two sensors matching them with the body segments and select a joint.", color = Color.White) },
            confirmButton = { TextButton(onClick = { calibViewModel.onShowNoSensorCalibReadyChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showJointsMismatch){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowJointsMismatchChange(false) },
            title = { Text(text = "Calibrate Sensors", color = Color.White) },
            text = { Text(text = "Mismatch between joints and segments selected.", color = Color.White) },
            confirmButton = { TextButton(onClick = { calibViewModel.onShowJointsMismatchChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showNoName){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { configViewModel.onShowNoNameChange(false) },
            title = { Text(text = "Calibrate Sensors", color = Color.White) },
            text = { Text(text = "Enter a file name.", color = Color.White) },
            confirmButton = { TextButton(onClick = { configViewModel.onShowNoNameChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showSensorCalib){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowSensorCalibChange(false) },
            title = { Text(text = "Calibrate Sensors", color = Color.White) },
            text = { Text(text = "Place the sensors together on a flat surface, with Z axes pointing up and the " +
                    "X axes heading in the same direction. Then, press Ok.", color = Color.White) },
            confirmButton = { TextButton(onClick = {
                calibViewModel.onShowSensorCalibChange(false)
                calibViewModel.onShowStartButtonChange(false)
                calibViewModel.onShowSensorCalibProgressChange(true)
                Timer(sensorListViewModel, calibViewModel, dataViewModel, selectedHz,
                    lifecycleOwner, appContext, recordingViewModel, configViewModel, avatarViewModel,dataWriter).runSensorCalib() }) {
                Text(text = "Ok", color = Color.Blue)
            }
            },
            dismissButton = {
                TextButton(onClick = { calibViewModel.onShowSensorCalibChange(false) }) {
                    Text(text = "Cancel", color = Color.Blue)
                }
            }
        )
    }
    if (calibDif){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { Restart(sensorListViewModel).startBad()
                sensorListViewModel.onCalibResultDifChange(false) },
            title = { Text(text = "Sensor Calibration", color = Color.White) },
            text = { Text(text = "Bad calibration. Restart ${sensorListViewModel.badSegments}.", color = Color.White) },
            confirmButton = { TextButton(onClick = { Restart(sensorListViewModel).startBad()
                sensorListViewModel.onCalibResultDifChange(false)}) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (calibDrift){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = {},
            title = { Text(text = "Sensor Calibration", color = Color.White) },
            text = { Text(text = "Sensors drift. Do you want to restart sensor calibration or continue?", color = Color.White) },
            confirmButton = { TextButton(onClick = {
                sensorListViewModel.onCalibResultDriftChange(false)
                calibViewModel.onShowStartModelCalibChange(true) }) {
                Text(text = "Continue", color = Color.Blue)
            }
            },
            dismissButton = {
                TextButton(onClick = {
                    Timer(sensorListViewModel, calibViewModel, dataViewModel, selectedHz,
                        lifecycleOwner, appContext, recordingViewModel, configViewModel, avatarViewModel, dataWriter).stopCalibRecording()
                    sensorListViewModel.onCalibResultDriftChange(false)
                    calibViewModel.onShowSensorCalibChange(true) }) {
                    Text(text = "Restart", color = Color.Blue)
                }
            }
        )
    }
    if (calibOk){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = {},
            title = { Text(text = "Good Calibration", color = Color.White) },
            text = { Text(text = "Successful sensor calibration. You can now start model calibration." +
                    "Press OK and stand still holding the calibration pose for 5 seconds. Then, bend joints following instructions.", color = Color.White) },
            confirmButton = { TextButton(onClick = {
                sensorListViewModel.onCalibResultOkChange(false)
                calibViewModel.onShowStaticChange(true)
                sensorListViewModel.onCleanBuffer()
                sensorListViewModel.onRecordingTypeChange("calib")
                Timer(sensorListViewModel, calibViewModel, dataViewModel, selectedHz,
                    lifecycleOwner, appContext, recordingViewModel, configViewModel, avatarViewModel, dataWriter).staticTimer()
            }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }

    if (showGoodCalibLevel){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowGoodModelCalibLevelChange(false) },
            title = { Text(text = "Model Calibration", color = Color.White) },
            text = { Text(text = "Good model calibration.", color = Color.White) },
            confirmButton = { TextButton(onClick = { calibViewModel.onShowGoodModelCalibLevelChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showPoorCalibLevel){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowPoorModelCalibLevelChange(false) },
            title = { Text(text = "Model Calibration", color = Color.White) },
            text = { Text(text = "Poor model calibration.", color = Color.White) },
            confirmButton = { TextButton(onClick = { calibViewModel.onShowPoorModelCalibLevelChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showBadCalibLevel){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { calibViewModel.onShowBadModelCalibLevelChange(false) },
            title = { Text(text = "Model Calibration", color = Color.White) },
            text = { Text(text = "Bad calibration. Restart calibration.", color = Color.White) },
            confirmButton = { TextButton(onClick = { calibViewModel.onShowBadModelCalibLevelChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (stopStreaming){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { recordingViewModel.onShowStopRecordingChange(false) },
            title = { Text(text = "Stop Streaming", color = Color.White) },
            text = { Text(text = "Stop recording first.", color = Color.White) },
            confirmButton = { TextButton(onClick = { recordingViewModel.onShowStopRecordingChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
                }
            }
        )
    }
}


