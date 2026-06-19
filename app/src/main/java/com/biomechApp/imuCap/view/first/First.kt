package com.biomechApp.imuCap.view.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.view.navegation.BottomBar
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun FirstScreen(navigationController: NavHostController,
                indexBottom:Int,
                configViewModel: ConfigViewModel,
                sensorListViewModel: SensorListViewModel,
                scannerViewModel: ScannerViewModel,
                dataViewModel: DataViewModel,
                recordingViewModel: RecordingViewModel,
                scanner: Scanner
) {
    val sensorList = sensorListViewModel.connectionLiveList
    val newText: String by sensorListViewModel.sensorConfigName.observeAsState(initial = "")
    val selectedSegment: String by sensorListViewModel.sensorConfigSegment.observeAsState(initial = "")
    var boxWidth: Float
    var boxHeight: Float

    Scaffold(
        topBar = { FirstTopBar(configViewModel, sensorListViewModel) },
        bottomBar = { BottomBar(
            navigationController,
            indexBottom,
            scanner,
            scannerViewModel,
            recordingViewModel
        ) }) {

        Box (modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .onGloballyPositioned { layoutCoordinates ->
                boxWidth = layoutCoordinates.size.width.toFloat()
                boxHeight = layoutCoordinates.size.height.toFloat()
                if (dataViewModel.screenWidth == 0f && dataViewModel.screenHeight == 0f){
                    dataViewModel.onScreenWidthChange(boxWidth-(boxWidth*0.1f))
                    dataViewModel.onScreenHeightChange(boxHeight-(boxHeight*0.05f))
                }
            }){

            LazyColumn{
                items(sensorList){sensorItem->
                    SensorView(
                        targetSensor = sensorItem,
                        sensorListViewModel = sensorListViewModel,
                    ) { sensor, action ->
                        if (action == "View") {
                            sensorListViewModel.onSensorConfigNameChange(sensor.name)
                            sensorListViewModel.onSensorConfigSegmentChange(sensor.segment)
                            sensorListViewModel.onCurrentSensorChange(sensor)
                        } else {
                            sensor.dotSensor.saveDeviceTag(newText)
                            sensorListViewModel.onSensorToSegmentChange(
                                sensor.address,
                                selectedSegment
                            )
                        }
                    }
                }
            }
        }
    }
}