package com.biomechApp.imuCap.view.third

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.biomechApp.imuCap.utils.Names
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel

@Composable
fun PlotConfig(plotViewModel: PlotViewModel, dataViewModel: DataViewModel) {

    val options = listOf("Joint", "Orientation", "None")
    val joints = Names().joints()
    val jointDoF = Names().jointsDoF()
    val segments = Names().segments()
    val segmentDoF = Names().segmentsDoF()

    val optHeight by plotViewModel.showPlotOptHeight.observeAsState(initial = 140.dp)
    val optWidth by plotViewModel.showPlotOptWidth.observeAsState(initial = 100.dp)
    val showOptList by plotViewModel.showPlotOpt.observeAsState(initial = options)
    val selectedDataList by dataViewModel.dataSelected.observeAsState(initial = MutableList(4) { "None" })

    val onSelectedChangeList = listOf(
        { value: String ->
            dataViewModel.onDataSelectedChange(value, 0)
            dataViewModel.onLegendSelectedChange(value, 0)
        },
        { value: String ->
            dataViewModel.onDataSelectedChange(value, 1)
            dataViewModel.onLegendSelectedChange(value, 1)
        },
        { value: String ->
            dataViewModel.onDataSelectedChange(value, 2)
            dataViewModel.onLegendSelectedChange(value, 2)
        },
        { value: String ->
            dataViewModel.onDataSelectedChange(value, 3)
            dataViewModel.onLegendSelectedChange(value, 3)
        }
    )

    val labelColors = listOf(Color.Green, Color.Cyan, Color.Red, Color.Yellow)
    val signalLabels = listOf("Signal 1:", "Signal 2:", "Signal 3:", "Signal 4:")
    val expandedStates = remember { List(4) { mutableStateOf(false) } }

    Dialog(onDismissRequest = {
        plotViewModel.onShowPlotConfigChange(false)
        plotViewModel.onShowPlotOptChange(options)
        plotViewModel.onShowPlotOptWidthChange(100.dp)
        plotViewModel.onShowPlotOptHeightChange(140.dp)
    }) {
        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp))) {
            Column(modifier = Modifier.background(Color.Gray)) {
                Text(
                    text = "Plot Configuration",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(5.dp, 5.dp, 80.dp, 20.dp)
                )

                (0..3).forEach { index ->
                    PlotDropdown(
                        plotViewModel,
                        label = signalLabels[index],
                        labelColor = labelColors[index],
                        selectedText = selectedDataList[index],
                        expandedState = expandedStates[index],
                        options = showOptList,
                        optHeight = optHeight,
                        optWidth = optWidth,
                        onDismiss = {
                            plotViewModel.onShowPlotOptChange(options)
                            plotViewModel.onShowPlotOptWidthChange(100.dp)
                            plotViewModel.onShowPlotOptHeightChange(140.dp)
                        },
                        onSelect = { selection ->
                            when (selection) {
                                "Joint" -> {
                                    plotViewModel.onShowPlotOptChange(joints)
                                    plotViewModel.onShowPlotOptWidthChange(150.dp)
                                    plotViewModel.onShowPlotOptHeightChange(200.dp)
                                }
                                "Orientation" -> {
                                    plotViewModel.onShowPlotOptChange(segments)
                                    plotViewModel.onShowPlotOptWidthChange(150.dp)
                                    plotViewModel.onShowPlotOptHeightChange(200.dp)
                                }
                                in joints -> {
                                    val idx = joints.indexOf(selection)
                                    plotViewModel.onShowPlotOptChange(jointDoF[idx])
                                    plotViewModel.onShowPlotOptWidthChange(170.dp)
                                    plotViewModel.onShowPlotOptHeightChange(140.dp)
                                }
                                in segments -> {
                                    val idx = segments.indexOf(selection)
                                    plotViewModel.onShowPlotOptChange(segmentDoF[idx])
                                    plotViewModel.onShowPlotOptWidthChange(150.dp)
                                    plotViewModel.onShowPlotOptHeightChange(140.dp)
                                }
                                else -> {
                                    expandedStates[index].value = false
                                    onSelectedChangeList[index](selection)
                                    plotViewModel.onShowPlotOptChange(options)
                                    plotViewModel.onShowPlotOptWidthChange(100.dp)
                                    plotViewModel.onShowPlotOptHeightChange(140.dp)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}

@Composable
fun PlotDropdown(
    plotViewModel: PlotViewModel,
    label: String,
    labelColor: Color,
    selectedText: String,
    expandedState: MutableState<Boolean>,
    options: List<String>,
    optHeight: Dp,
    optWidth: Dp,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit = {}
) {
    Row {
        Text(
            text = label,
            color = labelColor,
            modifier = Modifier.padding(start = 10.dp)
        )
        Box {
            Text(
                text = selectedText,
                color = Color.Blue,
                modifier = Modifier
                    .clickable { expandedState.value = true }
                    .padding(start = 20.dp)
            )
            DropdownMenu(
                expanded = expandedState.value,
                onDismissRequest = {
                    expandedState.value = false
                    onDismiss()
                },
                offset = DpOffset(x = 0.dp, y = 5.dp),
                modifier = Modifier
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .height(optHeight)
                        .width(optWidth)
                        .background(Color.White)
                ) {
                    LazyColumn {
                        items(options) {
                            DropdownMenuItem(
                                text = { Text(text = it, color = Color.Blue) },
                                onClick = { onSelect(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}