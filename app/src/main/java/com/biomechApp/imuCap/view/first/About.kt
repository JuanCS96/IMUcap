package com.biomechApp.imuCap.view.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun About(configViewModel: ConfigViewModel,
          sensorListViewModel: SensorListViewModel
) {

    val showAbout: Boolean by configViewModel.showAbout.observeAsState(initial = false)
    val options = listOf("Power Off", "About")

    DropdownMenu(
        offset = DpOffset(x = 1000.dp, y = 122.dp),
        modifier = Modifier
            .background(color = Color.Gray),
        expanded = showAbout,
        onDismissRequest = { configViewModel.onShowAboutChange(false) }) {
        Box(
            modifier = Modifier
                .padding()
                .height(100.dp)
                .width(100.dp)
                .background(color = Color.Gray)
        ) {
            LazyColumn {
                items(options) {
                    DropdownMenuItem(
                        text = { Text(text = it, color = Color.Blue) },
                        onClick = { configViewModel.onShowAboutChange(false)
                            if (it == "Power Off"){
                                if (sensorListViewModel.recordingType == "null" && sensorListViewModel.connectionLiveList.size == 0){
                                    sensorListViewModel.onShowPowerOffNoConnectedChange(true)
                                }else if (sensorListViewModel.recordingType != "null"){
                                    sensorListViewModel.onShowPowerOffNoNullChange(true)
                                }else {
                                    configViewModel.onShowPowerOffChange(true)
                                }
                            } else{
                                configViewModel.onShowAboutInfoChange(true)
                            }
                        }
                    )
                }
            }
        }
    }
}