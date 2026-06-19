package com.biomechApp.imuCap.view.fourth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.view.navegation.BottomBar
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import android.provider.Settings
import android.net.Uri
import android.os.Environment
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.UseCase
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.VideoRecordEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("ConstantLocale")
private val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())

@Composable
fun FourthScreen(
    navigationController: NavHostController,
    indexBottom: Int,
    configViewModel: ConfigViewModel,
    sensorListViewModel: SensorListViewModel,
    scannerViewModel: ScannerViewModel,
    plotViewModel: PlotViewModel,
    recordingViewModel: RecordingViewModel,
    dataWriter: Writer,
    scanner: Scanner,
    appContext: Context
) {

    val previewView = remember { PreviewView(appContext) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoCapture = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    val recording = remember { mutableStateOf<Recording?>(null) }

    val playStop: String by recordingViewModel.recordingState.observeAsState(initial = "play")
    val videoRecording: Boolean by recordingViewModel.videoRecordingState.observeAsState(initial = false)
    val videoRecordingInfo: Boolean by recordingViewModel.videoRecordingInfo.observeAsState(false)
    val showStreaming: Boolean by recordingViewModel.showIsStreaming.observeAsState(initial = false)
    val recordingSwitch: Int by recordingViewModel.recordingSwitch.observeAsState(initial = 2)
    val showRecordingSwitchInfo: Boolean by recordingViewModel.showRecordingSwitchInfo.observeAsState(false)

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
r
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = ContextCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                hasAudioPermission = ContextCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    if (!hasCameraPermission || !hasAudioPermission) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Camera/Audio permission denied.",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", appContext.packageName, null)
                }
                appContext.startActivity(intent)
            }) {
                Text("Open settings")
            }
        }
        return
    }

    LaunchedEffect(Unit) {
        startCameraPreview(appContext, lifecycleOwner, previewView) {
            videoCapture.value = it
        }
    }

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(appContext) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation in 0..359) {
                    val rotation = UseCase.snapToSurfaceRotation(orientation)
                    videoCapture.value?.targetRotation = rotation
                }
            }
        }
        orientationEventListener.enable()

        onDispose {
            orientationEventListener.disable()
        }
    }

    Scaffold(
        topBar = {
            FourthTopBar(
                plotViewModel,
                recordingViewModel,
                sensorListViewModel,
                configViewModel,
                dataWriter
            )
        },
        bottomBar = {
            BottomBar(
                navigationController,
                indexBottom,
                scanner,
                scannerViewModel,
                recordingViewModel
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            IconButton(
                onClick = {

                    if (sensorListViewModel.recordingType == "stream" && videoCapture != null){
                        if (playStop == "play" && recordingSwitch == 2){
                            recordingViewModel.onRecordingStateChange("stop")
                            recordingViewModel.onRecordingSwitchChange(1)
                            recordingViewModel.onVideoRecordingStateChange(true)
                            plotViewModel.onRecordingRectangleChange(Color.Red)
                            configViewModel.onRecordingFileNameEnableChange(false)
                            dataWriter.createCsv()

                            val file = File(
                                appContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                                "${configViewModel.fileOutName}_${dateFormat.format(Date())}.mp4"
                            )

                            val outputOptions = FileOutputOptions.Builder(file).build()
                            val recorderBuilder = videoCapture.value!!.output.prepareRecording(appContext, outputOptions)

                            val recordingPrepared = if (hasAudioPermission) {
                                if (ContextCompat.checkSelfPermission(
                                        appContext,
                                        Manifest.permission.RECORD_AUDIO
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    return@IconButton
                                }
                                recorderBuilder.withAudioEnabled()
                            } else {
                                recorderBuilder
                            }

                            recording.value = recordingPrepared.start(
                                ContextCompat.getMainExecutor(appContext)
                            ) { event ->
                                if (event is VideoRecordEvent.Finalize) {
                                    recording.value = null
                                }
                            }

                        }
                        else if (playStop == "stop" && recordingSwitch == 1) {
                            recordingViewModel.onRecordingStateChange("play")
                            recordingViewModel.onRecordingSwitchChange(2)
                            recordingViewModel.onVideoRecordingStateChange(false)
                            plotViewModel.onRecordingRectangleChange(Color.Black)
                            configViewModel.onRecordingFileNameEnableChange(true)
                            recordingViewModel.onStartWritingChange(false)
                            recording.value?.stop()
                            recording.value = null
                        }
                        else if (recordingSwitch == 0) {
                            recordingViewModel.onShowRecordingSwitchInfoChange(true)
                        }
                    } else{
                        recordingViewModel.onShowIsStreamingChange(true)
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = if (videoRecording) Color.Transparent else Color.Red,
                        shape = CircleShape
                    )
                    .border(4.dp,
                        color = Color.White,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (videoRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                    contentDescription = if (videoRecording) "Stop" else "Record",
                    tint = Color.Red,
                    modifier = Modifier.size(65.dp)
                )
            }
        }
    }
    if (showStreaming){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { recordingViewModel.onShowIsStreamingChange(false) },
            title = { Text(text = "Data Recording", color = Color.White) },
            text = { Text(text = "Start streaming first.", color = Color.White) },
            confirmButton = { TextButton(onClick = { recordingViewModel.onShowIsStreamingChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showRecordingSwitchInfo){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { recordingViewModel.onShowRecordingSwitchInfoChange(false) },
            title = { Text(text = "Data Recording", color = Color.White) },
            text = { Text(text = "Recording is already in progress.", color = Color.White) },
            confirmButton = { TextButton(onClick = { recordingViewModel.onShowRecordingSwitchInfoChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (videoRecordingInfo){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { recordingViewModel.onVideoRecordingInfoChange(false) },
            title = { Text(text = "Data Recording", color = Color.White) },
            text = { Text(text = "Stop video recording first.", color = Color.White) },
            confirmButton = { TextButton(onClick = { recordingViewModel.onVideoRecordingInfoChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
}



fun startCameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onVideoCaptureReady: (VideoCapture<Recorder>) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val rotation = previewView.display?.rotation ?: Surface.ROTATION_0

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.FHD))
            .build()

        val videoCapture = VideoCapture.withOutput(recorder)
        videoCapture.targetRotation = rotation

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            videoCapture
        )
        onVideoCaptureReady(videoCapture)

    }, ContextCompat.getMainExecutor(context))
}


