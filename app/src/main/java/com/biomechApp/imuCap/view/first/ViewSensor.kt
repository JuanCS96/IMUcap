package com.biomechApp.imuCap.view.first

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BatteryFull
import androidx.compose.material.icons.twotone.Flare
import androidx.compose.material.icons.twotone.RssFeed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.biomechApp.imuCap.model.Sensor
import com.biomechApp.imuCap.utils.Names
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun SensorView(targetSensor: Sensor, sensorListViewModel: SensorListViewModel, onApply:(Sensor, String)->Unit) {

    val newText: String by sensorListViewModel.sensorConfigName.observeAsState(initial = targetSensor.name)
    val selectedSegment: String by sensorListViewModel.sensorConfigSegment.observeAsState(initial = "Pelvis")
    val currentSensor: Sensor by sensorListViewModel.currentSensor.observeAsState(initial = targetSensor)
    var showSensorConfig by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val segments = Names().segments()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        onClick = {
            if (sensorListViewModel.recordingType=="null"){
                onApply(targetSensor, "View")
                showSensorConfig = true
            }else{
                sensorListViewModel.onShowPowerOffNoNullChange(true)
            }
        }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = Icons.TwoTone.RssFeed,
                        contentDescription = "dBm",
                        tint = Color.White
                    )
                    Text(
                        text = targetSensor.rssi.toString() + " dBm",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(top = 3.dp),
                            text = targetSensor.name,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                        )
                        Icon(
                            imageVector = Icons.TwoTone.BatteryFull,
                            contentDescription = "Blink",
                            tint = Color.White
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 3.dp),
                            text = targetSensor.battery.toString() + " %",
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 17.dp)
                    ) {
                        Text(
                            text = targetSensor.address,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        if (sensorListViewModel.recordingType=="null"){
                            targetSensor.dotSensor.identifyDevice()
                        }else{
                            sensorListViewModel.onShowPowerOffNoNullChange(true)
                        }
                    }) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = Icons.TwoTone.Flare,
                        contentDescription = "Blink",
                        tint = Color.Green
                    )
                }
            }
        }
    }
    if (showSensorConfig) {
        Dialog(
            onDismissRequest = { showSensorConfig = false }
        ) {
            Box (
                modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp, bottom = 20.dp),
                        text = "Sensor Configuration",
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp, bottom = 5.dp),
                        text = "Name:",
                        fontSize = 15.sp
                    )
                    TextField(
                        modifier = Modifier
                            .padding(5.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Gray,
                            unfocusedContainerColor = Color.Gray),
                        textStyle = TextStyle(color = Color.Blue),
                        value = newText,
                        onValueChange = {
                            sensorListViewModel.onSensorConfigNameChange(it)
                        },
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 5.dp,
                                bottom = 5.dp
                            ),
                        text = "Segment:",
                        fontSize = 15.sp
                    )
                    Text(
                        text = selectedSegment,
                        color = Color.Blue,
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(top = 15.dp, start = 20.dp)
                    )
                    Box{
                        DropdownMenu(
                            offset = DpOffset(x = 0.dp, y = 5.dp),
                            modifier = Modifier
                                .background(color = Color.White),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            Box(
                                modifier = Modifier
                                    .padding()
                                    .height(100.dp)
                                    .width(115.dp)
                                    .background(color = Color.White)
                            ) {
                                LazyColumn {
                                    items(segments) {
                                        DropdownMenuItem(
                                            text = { Text(text = it, color = Color.Blue) },
                                            onClick = { expanded = false; sensorListViewModel.onSensorConfigSegmentChange(it) })
                                    }
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.padding(start = 160.dp, top = 5.dp)) {
                        TextButton(
                            onClick = {
                                showSensorConfig = false
                            }
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color.Blue
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        TextButton(
                            onClick = {
                                onApply(currentSensor, "Sensor")
                                showSensorConfig = false
                            }
                        ) {
                            Text(
                                text = "Apply",
                                color = Color.Blue
                            )
                        }
                    }
                }
            }
        }
    }
}


