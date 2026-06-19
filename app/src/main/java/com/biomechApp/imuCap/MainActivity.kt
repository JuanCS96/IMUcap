package com.biomechApp.imuCap

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.model.Writer
import com.biomechApp.imuCap.view.navegation.Navigate
import com.biomechApp.imuCap.viewModel.CalibrationViewModel
import com.biomechApp.imuCap.viewModel.ConfigViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PermissionViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.biomechApp.imuCap.viewModel.RecordingViewModel
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.xsens.dot.android.sdk.DotSdk
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.net.Uri
import com.biomechApp.imuCap.view.first.MainScreen

class MainActivity : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        DotSdk.setDebugEnabled(false)
        DotSdk.setReconnectEnabled(true)

        val viewModelConfig by viewModels<ConfigViewModel>()
        val viewModelSensor by viewModels<SensorListViewModel>()
        val calibViewModel by viewModels<CalibrationViewModel>()
        val recordViewModel by viewModels<RecordingViewModel>()
        val scannerViewModel by viewModels<ScannerViewModel>()
        val dataViewModel by viewModels<DataViewModel>()
        val plotViewModel by viewModels<PlotViewModel>()
        val avatarViewModel by viewModels<AvatarViewModel>()
        val permissionViewModel by viewModels<PermissionViewModel>()

        val dotScanner = Scanner(this, viewModelSensor, scannerViewModel)
        val dataWriter = Writer(this, viewModelConfig, recordViewModel)

        setContent {
            MainScreen(
                permissionsToRequest = permissionsToRequest,
                permissionViewModel = permissionViewModel,
                dotScanner = dotScanner,
                onAllPermissionsGranted = {
                    Navigate(
                        viewModelConfig,
                        viewModelSensor,
                        calibViewModel,
                        recordViewModel,
                        scannerViewModel,
                        dataViewModel,
                        plotViewModel,
                        avatarViewModel,
                        dotScanner,
                        this,
                        this,
                        dataWriter
                    )
                }
            )
        }
    }
}

fun Activity.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(intent)
}
