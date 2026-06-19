package com.biomechApp.imuCap.view.fourth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.PlayCircle
import androidx.compose.material.icons.twotone.StopCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.biomechApp.imuCap.R
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FourthTopBar(
    plotViewModel: PlotViewModel,
    recordingViewModel: RecordingViewModel,
    sensorListViewModel: SensorListViewModel,
    configViewModel: ConfigViewModel,
    dataWriter: Writer
){

    val playStop: String by recordingViewModel.recordingState.observeAsState(initial = "play")
    val showIcon: ImageVector by  recordingViewModel.recordingIcon.observeAsState(initial = Icons.TwoTone.PlayCircle)
    val recordingSwitch: Int by recordingViewModel.recordingSwitch.observeAsState(initial = 2)

    TopAppBar(
        title = { Image(modifier = Modifier.size(225.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo")
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black,
            titleContentColor = Color.White),
        actions = {
            IconButton(modifier= Modifier.padding(10.dp),
                onClick = {

                    if (sensorListViewModel.recordingType == "stream"){
                        if (playStop == "play" && recordingSwitch == 2) {
                            recordingViewModel.onRecordingStateChange("stop")
                            recordingViewModel.onRecordingSwitchChange(0)
                            recordingViewModel.onRecordingIconChange(Icons.TwoTone.StopCircle)
                            plotViewModel.onRecordingRectangleChange(Color.Red)
                            configViewModel.onRecordingFileNameEnableChange(false)
                            dataWriter.createCsv()
                        }
                        else if (playStop == "stop" && recordingSwitch == 0) {
                            recordingViewModel.onRecordingStateChange("play")
                            recordingViewModel.onRecordingSwitchChange(2)
                            recordingViewModel.onRecordingIconChange(Icons.TwoTone.PlayCircle)
                            plotViewModel.onRecordingRectangleChange(Color.Black)
                            configViewModel.onRecordingFileNameEnableChange(true)
                            recordingViewModel.onStartWritingChange(false)
                        }
                        else if (recordingSwitch == 1) {
                            recordingViewModel.onShowRecordingSwitchInfoChange(true)
                        }
                    }
                    else{
                        recordingViewModel.onShowIsStreamingChange(true)
                    }
                }
            ) {
                Icon(imageVector = showIcon,
                    contentDescription = "Play",
                    tint = Color.White)
            }
        }
    )
}