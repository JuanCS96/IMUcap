package com.biomechApp.imuCap.view.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.biomechApp.imuCap.model.Filt
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun RecordingConfig(configViewModel: ConfigViewModel, sensorListViewModel: SensorListViewModel) {

    val selectedHz:String by configViewModel.selectedHz.observeAsState(initial = "30 Hz")
    val selectedFilt:String by configViewModel.selectedFilt.observeAsState(initial = "General")
    val appliedHz:String by configViewModel.appliedHz.observeAsState(initial = "30 Hz")
    val appliedFilt:String by configViewModel.appliedFilt.observeAsState(initial = "General")

    Dialog(
        onDismissRequest = {
            configViewModel.onShowRecordingConfigChange(false)
            configViewModel.onSelectedHzChange(appliedHz)
            configViewModel.onSelectedFiltChange(appliedFilt) }
    ) {
        Box (
            modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
        ){
            Column(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()){
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp, bottom = 20.dp),
                    text = "Sensor Settings",
                    fontSize = 20.sp,
                    color = Color.White)
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, bottom = 5.dp),
                    text = "Sampling Frequency:",
                    fontSize = 15.sp,
                    color = Color.White)
                Row {
                    RadioButton(
                        selected = selectedHz == "20 Hz",
                        onClick = { configViewModel.onSelectedHzChange("20 Hz") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.DarkGray
                        ))
                    Text(
                        modifier = Modifier
                            .padding(top = 14.dp),
                        text = "20 Hz",
                        color = Color.White)
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp))
                    RadioButton(
                        selected = selectedHz == "30 Hz",
                        onClick = { configViewModel.onSelectedHzChange("30 Hz") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.DarkGray
                        )
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 14.dp),
                        text = "30 Hz",
                        color = Color.White)
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp))
                    RadioButton(
                        selected = selectedHz == "60 Hz",
                        onClick = { configViewModel.onSelectedHzChange("60 Hz") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.DarkGray
                        ))
                    Text(
                        modifier = Modifier
                            .padding(top = 14.dp),
                        text = "60 Hz",
                        color = Color.White)
                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp))
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp,
                            bottom = 5.dp),
                    text = "Filter:",
                    fontSize = 15.sp,
                    color = Color.White)
                Row{
                    RadioButton(
                        selected = selectedFilt == "General",
                        onClick = { configViewModel.onSelectedFiltChange("General") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.DarkGray
                        ))
                    Text(
                        modifier = Modifier
                            .padding(top = 14.dp),
                        text = "General")
                    Spacer(
                        modifier = Modifier
                            .padding(15.dp)
                    )
                    RadioButton(
                        selected = selectedFilt == "Dynamic",
                        onClick = { configViewModel.onSelectedFiltChange("Dynamic") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.DarkGray
                        ))
                    Text(
                        modifier = Modifier
                            .padding(top = 14.dp),
                        text = "Dynamic")
                }
                Row {
                    TextButton(
                        modifier = Modifier.padding(start = 150.dp, top = 5.dp),
                        onClick = {
                            configViewModel.onShowRecordingConfigChange(false)
                            configViewModel.onSelectedHzChange(appliedHz)
                            configViewModel.onSelectedFiltChange(appliedFilt) }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Blue)
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(20.dp))
                    TextButton(
                        modifier = Modifier.padding(top = 5.dp),
                        onClick = {
                            configViewModel.onShowRecordingConfigChange(false)
                            sensorListViewModel.onXsensDotOutputRateChange(selectedHz)
                            configViewModel.onAppliedHzChange(selectedHz)
                            Filt(sensorListViewModel, selectedHz, selectedFilt).change()
                            configViewModel.onAppliedFiltChange(selectedFilt) }
                    ) {
                        Text(
                            text = "Apply",
                            color = Color.Blue)
                    }
                }
            }
        }
    }
}
