package com.biomechApp.imuCap.view.first

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.biomechApp.imuCap.viewModel.PermissionViewModel
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.biomechApp.imuCap.model.Scanner
import com.biomechApp.imuCap.openAppSettings
import com.biomechApp.imuCap.utils.BluetoothPermissionTextProvider
import com.biomechApp.imuCap.utils.CameraPermissionTextProvider
import com.biomechApp.imuCap.utils.LocationPermissionTextProvider
import com.biomechApp.imuCap.utils.MicrophonePermissionTextProvider
import com.biomechApp.imuCap.utils.PermissionDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    permissionsToRequest: Array<String>,
    permissionViewModel: PermissionViewModel,
    dotScanner: Scanner,
    onAllPermissionsGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        permissionsToRequest.forEach { permission ->
            permissionViewModel.onPermissionResult(
                permission = permission,
                isGranted = perms[permission] == true
            )
        }
    }

    LaunchedEffect(Unit) {
        multiplePermissionResultLauncher.launch(permissionsToRequest)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        dialogQueue.reversed().forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                    Manifest.permission.RECORD_AUDIO -> MicrophonePermissionTextProvider()
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT -> BluetoothPermissionTextProvider()
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION -> LocationPermissionTextProvider()

                    else -> return@forEach
                },
                isPermanentlyDeclined = activity?.let {
                    !ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                } ?: false,
                onDismiss = permissionViewModel::dismissDialog,
                onOkClick = {
                    permissionViewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(arrayOf(permission))
                },
                onGoToAppSettingsClick = {
                    activity?.openAppSettings()
                }
            )
        }

        if (dialogQueue.isEmpty()) {
            onAllPermissionsGranted()
        }

        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        if (permissionsState.allPermissionsGranted) {
            dotScanner.startScan()
        }
    }
}