package com.biomechApp.imuCap.view.navegation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Accessibility
import androidx.compose.material.icons.twotone.Insights
import androidx.compose.material.icons.twotone.Sensors
import androidx.compose.material.icons.twotone.VideoCameraFront
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel


@Composable
fun BottomBar(navController: NavHostController,
              indexBottom:Int,
              scanner: Scanner,
              scannerViewModel: ScannerViewModel,
              recordingViewModel: RecordingViewModel
){

    val scannerState: Boolean by scannerViewModel.scannerState.observeAsState(initial = true)
    val videoRecording: Boolean by recordingViewModel.videoRecordingState.observeAsState(false)

    BottomAppBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = false,
            onClick = {
                if (videoRecording) {
                    recordingViewModel.onVideoRecordingInfoChange(true)
                }
                else {
                    navController.navigate(Routes.Screen1.route)
                    if (!scannerState){scanner.startScan()}
                }
            },
            icon = { Icon(modifier = Modifier.fillMaxSize(),
                imageVector = Icons.TwoTone.Sensors,
                contentDescription = "Sensors",
                tint = if (indexBottom == 1) Color.Blue else Color.Gray
            )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                if (videoRecording) {
                    recordingViewModel.onVideoRecordingInfoChange(true)
                }
                else {
                    navController.navigate(Routes.Screen2.route)
                    if (scannerState){scanner.stopScan()}
                }
            },
            icon = { Icon(modifier = Modifier.fillMaxSize(),
                imageVector = Icons.TwoTone.Accessibility,
                contentDescription = "Calibration",
                tint = if (indexBottom == 2) Color.Blue else Color.Gray
            )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                if (videoRecording) {
                    recordingViewModel.onVideoRecordingInfoChange(true)
                }
                else {
                    navController.navigate(Routes.Screen3.route)
                    if (scannerState){scanner.stopScan()}
                }
            },
            icon = { Icon(modifier = Modifier.fillMaxSize(),
                imageVector = Icons.TwoTone.Insights,
                contentDescription = "Plots",
                tint = if (indexBottom == 3) Color.Blue else Color.Gray
            )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Routes.Screen4.route)
                if (scannerState){scanner.stopScan()}
            },
            icon = { Icon(modifier = Modifier.fillMaxSize(),
                imageVector = Icons.TwoTone.VideoCameraFront,
                contentDescription = "Video",
                tint = if (indexBottom == 4) Color.Blue else Color.Gray
            )
            }
        )
    }
}