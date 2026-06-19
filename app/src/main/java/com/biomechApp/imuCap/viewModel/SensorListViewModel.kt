package com.biomechApp.imuCap.viewModel

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biomechApp.imuCap.model.CalibResult
import com.biomechApp.imuCap.model.DynamicCalib
import com.biomechApp.imuCap.model.Sensor
import com.biomechApp.imuCap.model.StaticCalib
import com.biomechApp.imuCap.view.second.Joint
import com.xsens.dot.android.sdk.events.DotData
import com.xsens.dot.android.sdk.interfaces.DotDeviceCallback
import com.xsens.dot.android.sdk.interfaces.DotMeasurementCallback
import com.xsens.dot.android.sdk.models.DotDevice
import com.xsens.dot.android.sdk.models.DotPayload
import com.xsens.dot.android.sdk.models.FilterProfileInfo
import java.util.ArrayList
import kotlin.String
import kotlin.collections.get

class SensorListViewModel: ViewModel(), DotDeviceCallback, DotMeasurementCallback {

    private val handler = Handler(Looper.getMainLooper())

    private var rssiValuesLiveList = mutableListOf<HashMap<String, Int>>()

    var sensorLiveList = mutableStateListOf<DotDevice>()
        private set

    var connectionFlowList = mutableStateListOf<DotDevice>()
        private set

    var enableConnect = true

    fun onEnableConnectChange(state:Boolean){
        enableConnect = state
    }

    fun addSensor(bluetoothDevice: BluetoothDevice?, context: Context, rssi: Int){
        val dotDevice = DotDevice(context, bluetoothDevice, this)
        if (!sensorLiveList.any{it.address == dotDevice.address} && dotDevice.name != null){
            val sensorHashMap: HashMap<String, Int> = hashMapOf(dotDevice.address to rssi)
            rssiValuesLiveList.add(sensorHashMap)
            dotDevice.measurementMode = DotPayload.PAYLOAD_TYPE_CUSTOM_MODE_5
            sensorLiveList.add(dotDevice)
            connectionFlowList.add(dotDevice)
            dotDevice.connect()
        }
    }

    override fun onDotConnectionChanged(address: String?, state: Int) {
        val sensor = connectionLiveList.find { it.address == address }
        val dotSensor = sensorLiveList.find { it.address == address }
        if (state == DotDevice.CONN_STATE_DISCONNECTED){
            handler.post {
                sensorLiveList.removeIf { it == dotSensor }
                connectionLiveList.removeIf { it == sensor }
            }
        }
    }

    fun removeAllSensors(){
        sensorLiveList.forEach {sensor ->
            sensor.powerOffDevice()
        }
    }

    private val _showPowerOffNoNull = MutableLiveData<Boolean>()
    val showPowerOffNoNull : LiveData<Boolean> = _showPowerOffNoNull

    private val _showPowerOffNoConnected = MutableLiveData<Boolean>()
    val showPowerOffNoConnected : LiveData<Boolean> = _showPowerOffNoConnected

    fun onShowPowerOffNoNullChange(showPowerOff:Boolean){
        _showPowerOffNoNull.value = showPowerOff
    }

    fun onShowPowerOffNoConnectedChange(showPowerOff:Boolean){
        _showPowerOffNoConnected.value = showPowerOff
    }

    var connectionLiveList = mutableStateListOf<Sensor>()
        private set

    private var initState = false

    override fun onDotInitDone(address: String?) {
        initState = true
        val foundSensor = sensorLiveList.find { it.address == address }
        if (foundSensor != null) {
            foundSensor.setFilterProfile(0)

            if (!connectionLiveList.any { it.address == address }) {
                var rssiValue: Int? = null
                for (map in rssiValuesLiveList) {
                    if (map.containsKey(address)) {
                        rssiValue = map[address]
                        break
                    }
                }
                connectionLiveList.add(
                    Sensor(
                        address = foundSensor.address,
                        name = foundSensor.tag,
                        battery = foundSensor.batteryPercentage,
                        rssi = rssiValue!!,
                        dotSensor = foundSensor,
                        segment = "Pelvis"
                    )
                )
                connectionFlowList.removeAt(0)
                onEnableConnectChange(true)
            }
        }
    }

    override fun onDotFilterProfileUpdate(address: String?, filter: Int) {
        val foundSensor = sensorLiveList.find { it.address == address }
        if (initState && foundSensor != null){
            foundSensor.setOutputRate(30)
            initState = false
        }
    }

    fun onXsensDotOutputRateChange(outputRate:String) {
        val outputRateValue = Regex("\\d+").find(outputRate)?.value!!.toInt()
        for (dotSensor in sensorLiveList){
            dotSensor.setOutputRate(outputRateValue)
        }
    }

    fun onXsensDotFilterProfileChange(filterProfile:String) {
        for (dotSensor in sensorLiveList){
            if (filterProfile=="General") {
                dotSensor.setFilterProfile(0)
            }else {
                dotSensor.setFilterProfile(1)
            }
        }
    }

    override fun onDotTagChanged(address: String?, tag: String?) {
        val foundSensor = connectionLiveList.find { it.address == address }
        if (foundSensor != null){
            foundSensor.name = tag.toString()
        }
    }

    val segmentSelected = MutableList(15) { "None" }
    private fun clearSegmentSelected() {
        for (i in 0 until segmentSelected.size) {
            segmentSelected[i] = "None"
        }
    }

    private val _jointTicks = mutableStateListOf(
        Joint("neck", 0.47f, 0.14f),
        Joint("lumbar", 0.47f, 0.33f),
        Joint("r_hip", 0.34f, 0.46f),
        Joint("l_hip", 0.58f, 0.46f),
        Joint("r_knee", 0.36f, 0.65f),
        Joint("l_knee", 0.56f, 0.65f),
        Joint("r_ankle", 0.38f, 0.85f),
        Joint("l_ankle", 0.55f, 0.85f),
        Joint("r_shoulder", 0.27f, 0.21f),
        Joint("l_shoulder", 0.66f, 0.21f),
        Joint("r_elbow", 0.22f, 0.35f),
        Joint("l_elbow", 0.69f, 0.35f),
        Joint("r_wrist", 0.17f, 0.48f),
        Joint("l_wrist", 0.74f, 0.48f)
    )

    val jointTicks: SnapshotStateList<Joint> = _jointTicks

    fun toggleJointTicks(name: String) {
        val index = _jointTicks.indexOfFirst { it.name == name }
        if (index != -1) {
            val joint = _jointTicks[index]
            _jointTicks[index] = joint.copy(selected = !joint.selected)
        }
    }

    var jointSelected = mutableStateListOf<String>()

    fun toggleJointSelection(jointName: String) {
        if (jointSelected.contains(jointName)) {
            jointSelected.remove(jointName)
        } else {
            jointSelected.add(jointName)
        }
    }

    private fun jointSegment(segment1: String, segment2: String): Boolean {
        return connectionLiveList.any { it.segment == segment1 } &&
                connectionLiveList.any { it.segment == segment2 }
    }

    fun onCheckNumSensors(): Boolean {
        return segmentSelected.count { it != "None" } == connectionLiveList.size
    }

    fun onCheckJointsSelected(): Boolean {
        clearSegmentSelected()
        var output = true
        for (joint in jointSelected) {
            when (joint){
                "neck" -> {
                    if (!jointSegment("Torso", "Head")) {
                        output = false
                    }
                    else {
                        segmentSelected[1] = "torso"
                        segmentSelected[2] = "head"
                    }
                }
                "lumbar" -> {
                    if (!jointSegment("Pelvis", "Torso")) {
                        output = false
                    }
                    else {
                        segmentSelected[0] = "pelvis"
                        segmentSelected[1] = "torso"
                    }
                }
                "r_hip" -> {
                    if (!jointSegment("Pelvis", "Right Thigh")) {
                        output = false
                    }
                    else {
                        segmentSelected[0] = "pelvis"
                        segmentSelected[3] = "right thigh"
                    }
                }
                "l_hip" -> {
                    if (!jointSegment("Pelvis", "Left Thigh")) {
                        output = false
                    }
                    else {
                        segmentSelected[0] = "pelvis"
                        segmentSelected[4] = "left thigh"
                    }
                }
                "r_knee" -> {
                    if (!jointSegment("Right Thigh", "Right Shank")) {
                        output = false
                    }
                    else {
                        segmentSelected[3] = "right thigh"
                        segmentSelected[5] = "right shank"
                    }
                }
                "l_knee" -> {
                    if (!jointSegment("Left Thigh", "Left Shank")) {
                        output = false
                    }
                    else {
                        segmentSelected[4] = "left thigh"
                        segmentSelected[6] = "left shank"
                    }
                }
                "r_ankle" -> {
                    if (!jointSegment("Right Shank", "Right Foot")) {
                        output = false
                    }
                    else {
                        segmentSelected[5] = "right shank"
                        segmentSelected[7] = "right foot"
                    }
                }
                "l_ankle" -> {
                    if (!jointSegment("Left Shank", "Left Foot")) {
                        output = false
                    }
                    else {
                        segmentSelected[6] = "left shank"
                        segmentSelected[8] = "left foot"
                    }
                }
                "r_shoulder" -> {
                    if (!jointSegment("Torso", "Right Arm")) {
                        output = false
                    }
                    else {
                        segmentSelected[1] = "torso"
                        segmentSelected[9] = "right arm"
                    }
                }
                "l_shoulder" -> {
                    if (!jointSegment("Torso", "Left Arm")) {
                        output = false
                    }
                    else {
                        segmentSelected[1] = "torso"
                        segmentSelected[10] = "left arm"
                    }
                }
                "r_elbow" -> {
                    if (!jointSegment("Right Arm", "Right Forearm")) {
                        output = false
                    }
                    else {
                        segmentSelected[9] = "right arm"
                        segmentSelected[11] = "right forearm"
                    }
                }
                "l_elbow" -> {
                    if (!jointSegment("Left Arm", "Left Forearm")) {
                        output = false
                    }
                    else {
                        segmentSelected[10] = "left arm"
                        segmentSelected[12] = "left forearm"
                    }
                }
                "r_wrist" -> {
                    if (!jointSegment("Right Forearm", "Right Hand")) {
                        output = false
                    }
                    else {
                        segmentSelected[11] = "right forearm"
                        segmentSelected[13] = "right hand"
                    }
                }
                "l_wrist" -> {
                    if (!jointSegment("Left Forearm", "Left Hand")) {
                        output = false
                    }
                    else {
                        segmentSelected[12] = "left forearm"
                        segmentSelected[14] = "left hand"
                    }
                }
            }
        }
        return output
    }

    var recordingState = false

    fun onRecordingStateChange(state:Boolean){
        recordingState = state
    }

    var recordingType = "null"

    fun onRecordingTypeChange(type:String){
        recordingType = type
    }

    val buffers = List(15) { mutableListOf<DotData>() }

    fun onCleanBuffer(){
        for (buffer in buffers) {
            buffer.clear()
        }
    }

    override fun onDotDataChanged(address: String?, data: DotData?) {
        val foundSensor = connectionLiveList.find { it.address == address }
        if (recordingType=="calib" || recordingType=="stream"){
            when (foundSensor!!.segment){
                "Pelvis" -> {
                    buffers[0].add(data!!)
                }
                "Torso" -> {
                    buffers[1].add(data!!)
                }
                "Head" -> {
                    buffers[2].add(data!!)
                }
                "Right Thigh" -> {
                    buffers[3].add(data!!)
                }
                "Left Thigh" -> {
                    buffers[4].add(data!!)
                }
                "Right Shank" -> {
                    buffers[5].add(data!!)
                }
                "Left Shank" -> {
                    buffers[6].add(data!!)
                }
                "Right Foot" -> {
                    buffers[7].add(data!!)
                }
                "Left Foot" -> {
                    buffers[8].add(data!!)
                }
                "Right Arm" -> {
                    buffers[9].add(data!!)
                }
                "Left Arm" -> {
                    buffers[10].add(data!!)
                }
                "Right Forearm" -> {
                    buffers[11].add(data!!)
                }
                "Left Forearm" -> {
                    buffers[12].add(data!!)
                }
                "Right Hand" -> {
                    buffers[13].add(data!!)
                }
                "Left Hand" -> {
                    buffers[14].add(data!!)
                }
            }
        }
    }

    fun calibDataEmitter(calibViewModel: CalibrationViewModel,
                         configViewModel: ConfigViewModel,
                         rate: Int
    ){
        CalibResult(buffers, this, calibViewModel, configViewModel, rate)
        onCleanBuffer()
    }

    fun staticDataEmitter(rate: Int) {
        StaticCalib(buffers, this, rate)
    }

    fun dynamicDataEmitter(
        rate: Int,
        calibViewModel: CalibrationViewModel,
        configViewModel: ConfigViewModel,
        segmentCount: Int,
        reversedSegmentSelected: List<String>
    ) {
        DynamicCalib(buffers, this, calibViewModel, configViewModel, rate, segmentCount, reversedSegmentSelected)
        onCleanBuffer()
    }

    var staticSegments: List<MutableList<Float>> = listOf()
        private set

    fun onStaticSegmentsChange(data:List<MutableList<Float>>){
        staticSegments = data
    }

    var localFrames: List<List<Float>> = listOf()
        private set

    fun onLocalFramesChange(data:List<List<Float>>){
        localFrames = data
    }

    private val _calibResultDif = MutableLiveData<Boolean>()
    val calibResultDif : LiveData<Boolean> = _calibResultDif

    fun onCalibResultDifChange(calibResultDif:Boolean){
        _calibResultDif.value = calibResultDif
    }

    private val _calibResultDrift = MutableLiveData<Boolean>()
    val calibResultDrift : LiveData<Boolean> = _calibResultDrift

    fun onCalibResultDriftChange(calibResultDrift:Boolean){
        _calibResultDrift.value = calibResultDrift
    }

    private val _calibResultOk = MutableLiveData<Boolean>()
    val calibResultOk : LiveData<Boolean> = _calibResultOk

    fun onCalibResultOkChange(calibResultOk:Boolean){
        _calibResultOk.value = calibResultOk
    }

    var badSegments: String = ""
        private set

    fun onBadSegmentsChange(segments:ArrayList<String>){
        var sentence: String = ""
        var count = 0
        for (segment in segments){
            if (count != 0){
                sentence += ", $segment"
            }else {
                sentence += segment
            }
            count += 1
        }
        badSegments = sentence
    }

    private val _sensorConfigName = MutableLiveData<String>()
    val sensorConfigName : LiveData<String> = _sensorConfigName

    private val _sensorConfigSegment = MutableLiveData<String>()
    val sensorConfigSegment : LiveData<String> = _sensorConfigSegment

    private val _currentSensor = MutableLiveData<Sensor>()
    val currentSensor : LiveData<Sensor> = _currentSensor


    fun onSensorConfigNameChange(sensorConfigName:String){
        _sensorConfigName.value =
            if (sensorConfigName.length <= 11) {
                sensorConfigName
            } else {
                _sensorConfigName.value
            }
    }

    fun onSensorConfigSegmentChange(segment:String){
        _sensorConfigSegment.value = segment
    }

    fun onCurrentSensorChange(currentSensor: Sensor){
        _currentSensor.value = currentSensor
    }

    fun onSensorToSegmentChange(address:String, segment:String){
        val foundSensor = connectionLiveList.find { it.address == address }
        if (foundSensor != null){
            foundSensor.segment = segment
        }
    }

    fun checkDuplicateSegments(): Boolean{
        val segments = mutableListOf<String>()
        for (sensor in connectionLiveList){
            segments.add(sensor.segment)
        }
        val unique = segments.distinct()
        return segments.size != unique.size
    }

    override fun onDotServicesDiscovered(address: String?, status: Int) {

    }

    override fun onDotBatteryChanged(p0: String?, p1: Int, p2: Int) {

    }

    override fun onDotFirmwareVersionRead(p0: String?, p1: String?) {

    }

    override fun onDotButtonClicked(p0: String?, p1: Long) {

    }

    override fun onDotPowerSavingTriggered(p0: String?) {

    }

    override fun onReadRemoteRssi(p0: String?, p1: Int) {

    }

    override fun onDotOutputRateUpdate(p0: String?, p1: Int) {

    }

    override fun onDotGetFilterProfileInfo(p0: String?, p1: ArrayList<FilterProfileInfo>?) {

    }

    override fun onSyncStatusUpdate(p0: String?, p1: Boolean) {

    }

    var resetHeadingDone = false
    fun resetHeading(){
        for (sensor in sensorLiveList) {
            sensor.resetHeading()
        }
    }

    override fun onDotHeadingChanged(address: String?, status: Int, result: Int) {

    }

    override fun onDotRotLocalRead(address: String?, quat: FloatArray?) {

    }
}
