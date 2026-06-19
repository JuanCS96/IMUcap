package com.biomechApp.imuCap.view.first

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biomechApp.imuCap.R
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstTopBar(configViewModel: ConfigViewModel,
                   sensorListViewModel: SensorListViewModel) {

    val showRecordingConfig: Boolean by configViewModel.showRecordingConfig.observeAsState(initial = false)
    val showAbout: Boolean by configViewModel.showAbout.observeAsState(initial = false)
    val showPowerOff: Boolean by configViewModel.showPowerOff.observeAsState(initial = false)
    val showNoNull: Boolean by sensorListViewModel.showPowerOffNoNull.observeAsState(initial = false)
    val showNoConnected: Boolean by sensorListViewModel.showPowerOffNoConnected.observeAsState(initial = false)
    val showAboutInfo: Boolean by configViewModel.showAboutInfo.observeAsState(initial = false)

    TopAppBar(
        title = { Image(modifier = Modifier.size(225.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo")},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black,
            titleContentColor = Color.White),
        actions = {
            IconButton(modifier=Modifier.padding(10.dp),
                onClick = {
                    if (sensorListViewModel.recordingType == "null" && sensorListViewModel.connectionLiveList.size == 0){
                        sensorListViewModel.onShowPowerOffNoConnectedChange(true)
                    }else if (sensorListViewModel.recordingType != "null"){
                        sensorListViewModel.onShowPowerOffNoNullChange(true)
                    }else {
                        configViewModel.onShowRecordingConfigChange(true)
                    }
                }
            ) {
                Icon(imageVector = Icons.TwoTone.Settings,
                    contentDescription = "Settings",
                    tint = Color.White)
            }

            IconButton(modifier=Modifier.padding(10.dp),
                onClick = { configViewModel.onShowAboutChange(true) }) {
                Icon(imageVector = Icons.TwoTone.MoreVert,
                    contentDescription = "Help",
                    tint = Color.White)
            }
        }
    )

    if(showRecordingConfig){
        RecordingConfig(configViewModel, sensorListViewModel)
    }
    if(showAbout){
        About(configViewModel, sensorListViewModel)
    }
    if (showPowerOff){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { configViewModel.onShowPowerOffChange(false) },
            title = { Text(text = "Power Off Sensors")},
            text = { Text(text = "Are you sure to power off all sensors?")},
            confirmButton = { TextButton(onClick = { sensorListViewModel.removeAllSensors()
                configViewModel.onShowPowerOffChange(false)}) {
                Text(text = "Confirm", color = Color.Blue)
            }
            },
            dismissButton = {TextButton(onClick = { configViewModel.onShowPowerOffChange(false) }) {
                Text(text = "Cancel", color = Color.Blue)
            }
            }
        )
    }
    if (showNoConnected){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { sensorListViewModel.onShowPowerOffNoConnectedChange(false) },
            title = { Text(text = "Sensor Connection") },
            text = { Text(text = "No sensor connected.") },
            confirmButton = { TextButton(onClick = { sensorListViewModel.onShowPowerOffNoConnectedChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showNoNull){
        AlertDialog(
            containerColor = Color.Gray,
            onDismissRequest = { sensorListViewModel.onShowPowerOffNoNullChange(false) },
            title = { Text(text = "Sensor Measurement") },
            text = { Text(text = "Stop streaming first.") },
            confirmButton = { TextButton(onClick = { sensorListViewModel.onShowPowerOffNoNullChange(false) }) {
                Text(text = "Ok", color = Color.Blue)
            }
            }
        )
    }
    if (showAboutInfo){
        Dialog(
            onDismissRequest = { configViewModel.onShowAboutInfoChange(false) }
        ){
            Box (
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
            ){
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                ){
                    Box (
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .height(100.dp)
                    ){
                        Image(modifier = Modifier
                            .size(225.dp)
                            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo")
                    }
                    Box (
                        modifier = Modifier
                            .height(50.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 0.dp, bottom = 20.dp),
                            text = "Version 2025.1.0",
                            fontSize = 15.sp,
                            color = Color.White)
                    }
                    Box (
                        modifier = Modifier
                            .height(50.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 0.dp, bottom = 20.dp),
                            text = "github.com/JuanCS96/IMUcap",
                            fontSize = 15.sp,
                            color = Color.White)
                    }
                }
            }
        }
    }
}