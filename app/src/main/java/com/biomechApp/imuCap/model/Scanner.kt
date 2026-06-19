package com.biomechApp.imuCap.model

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanSettings
import android.content.Context
import com.biomechApp.imuCap.viewModel.ScannerViewModel
import com.biomechApp.imuCap.viewModel.SensorListViewModel
import com.xsens.dot.android.sdk.interfaces.DotScannerCallback
import com.xsens.dot.android.sdk.utils.DotScanner


class Scanner(context: Context,
              val sensorListViewModel: SensorListViewModel,
              private val scannerViewModel: ScannerViewModel): DotScannerCallback {

    private val appContext = context
    private var dotScanner: DotScanner? = null

    fun startScan(){

        dotScanner = DotScanner(appContext, this)
        dotScanner!!.setScanMode(ScanSettings.SCAN_MODE_BALANCED)

        scannerViewModel.onScannerStateChange(true)
        dotScanner!!.startScan()
    }

    override fun onDotScanned(device: BluetoothDevice?, rssi: Int) {
        sensorListViewModel.addSensor(device, appContext, rssi)
    }

    fun stopScan(){
        scannerViewModel.onScannerStateChange(false)
        dotScanner!!.stopScan()
    }
}