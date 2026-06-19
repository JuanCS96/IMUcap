package com.biomechApp.imuCap.view.third

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.view.navegation.BottomBar
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel


@Composable
fun ThirdScreen(navigationController: NavHostController,
                indexBottom:Int,
                scannerViewModel: ScannerViewModel,
                plotViewModel: PlotViewModel,
                dataViewModel: DataViewModel,
                recordingViewModel: RecordingViewModel,
                avatarViewModel: AvatarViewModel,
                scanner: Scanner,
                appContext: Context
) {
    Scaffold(
        topBar = { ThirdTopBar(plotViewModel, dataViewModel, avatarViewModel, appContext) },
        bottomBar = { BottomBar(
            navigationController,
            indexBottom,
            scanner,
            scannerViewModel,
            recordingViewModel
        ) }) {

        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(Color.Black)) {

            Plot(plotViewModel, dataViewModel)

        }
    }
}

