package com.biomechApp.imuCap.view.navegation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.view.second.SecondScreen
import com.biomechApp.imuCap.view.third.ThirdScreen
import com.biomechApp.imuCap.view.first.FirstScreen
import com.biomechApp.imuCap.view.fourth.FourthScreen
import com.biomechApp.imuCap.view.splash.SplashScreen
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel

@Composable
fun Navigate(viewModelConfig: ConfigViewModel,
             viewModelSensor: SensorListViewModel,
             calibViewModel: CalibrationViewModel,
             recordViewModel: RecordingViewModel,
             scannerViewModel: ScannerViewModel,
             dataViewModel: DataViewModel,
             plotViewModel: PlotViewModel,
             avatarViewModel: AvatarViewModel,
             scanner: Scanner,
             lifecycleOwner: LifecycleOwner,
             appContext: Context,
             dataWriter: Writer
) {

    val navigationController = rememberNavController()

    NavHost(navController = navigationController, startDestination = Routes.Screen0.route) {
        composable(Routes.Screen0.route){
            SplashScreen(navigationController)
        }
        composable(Routes.Screen1.route) {
            FirstScreen(
                navigationController, 1,
                viewModelConfig,
                viewModelSensor,
                scannerViewModel,
                dataViewModel,
                recordViewModel,
                scanner
            )
        }
        composable(Routes.Screen2.route) {
            SecondScreen(
                navigationController, 2,
                viewModelConfig,
                viewModelSensor,
                calibViewModel,
                recordViewModel,
                scannerViewModel,
                dataViewModel,
                avatarViewModel,
                scanner,
                lifecycleOwner,
                appContext,
                dataWriter
            )
        }
        composable(Routes.Screen3.route) {
            ThirdScreen(
                navigationController, 3,
                scannerViewModel,
                plotViewModel,
                dataViewModel,
                recordViewModel,
                avatarViewModel,
                scanner,
                appContext
            )
        }
        composable(Routes.Screen4.route) {
            FourthScreen(
                navigationController, 4,
                viewModelConfig,
                viewModelSensor,
                scannerViewModel,
                plotViewModel,
                recordViewModel,
                dataWriter,
                scanner,
                appContext
            )
        }
    }
}
