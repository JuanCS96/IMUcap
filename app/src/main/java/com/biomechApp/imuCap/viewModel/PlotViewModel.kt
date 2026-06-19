package com.biomechApp.imuCap.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlotViewModel: ViewModel() {

    private val _showPlotConfig = MutableLiveData<Boolean>()
    val showPlotConfig : LiveData<Boolean> = _showPlotConfig
    fun onShowPlotConfigChange(showPlotConfig:Boolean){
        _showPlotConfig.value = showPlotConfig
    }

    private val _showPlotOpt = MutableLiveData<List<String>>()
    val showPlotOpt : LiveData<List<String>> = _showPlotOpt
    fun onShowPlotOptChange(jointsOpt:List<String>){
        _showPlotOpt.value = jointsOpt
    }

    private val _onRecordingRectangle = MutableLiveData<Color>()
    val onRecordingRectangle : LiveData<Color> = _onRecordingRectangle
    fun onRecordingRectangleChange(rect:Color){
        _onRecordingRectangle.value = rect
    }

    private val _showPlotOptHeight = MutableLiveData<Dp>()
    val showPlotOptHeight : LiveData<Dp> = _showPlotOptHeight
    fun onShowPlotOptHeightChange(height:Dp){
        _showPlotOptHeight.value = height
    }

    private val _showPlotOptWidth = MutableLiveData<Dp>()
    val showPlotOptWidth : LiveData<Dp> = _showPlotOptWidth
    fun onShowPlotOptWidthChange(width:Dp){
        _showPlotOptWidth.value = width
    }
}