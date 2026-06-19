package com.biomechApp.imuCap.view.third

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel

@Composable
fun Legend(text: TextMeasurer, signal: String, color: Color) {

    val legendTextStyle = TextStyle(
        fontSize = 13.sp,
        color = color,
    )

    val measuredText =
        text.measure(
            AnnotatedString(signal),
            constraints = Constraints.fixedWidth((500).toInt()),
            style = legendTextStyle,
        )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        rotate(degrees = 90F, pivot = Offset(0F, 0F)) {
            drawText(
                measuredText,
                topLeft = Offset(
                    x = measuredText.size.width*0.15F,
                    y = -size.width*0.85F
                )
            )
        }
    }
}

@Composable
fun Plot(plotViewModel: PlotViewModel, dataViewModel: DataViewModel) {

    val signals by dataViewModel.dataSelected.observeAsState(initial = List(4) { "None" })

    val axes by dataViewModel.axes.observeAsState(initial = listOf(
        "0.0", "10.0", "20.0", "30.0", "40.0", "50.0", "60.0"))

    val signalColors = listOf(Color.Green, Color.Cyan, Color.Red, Color.Yellow)
    val rectColor: Color by plotViewModel.onRecordingRectangle.observeAsState(initial = Color.Black)

    Row {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.1f)
                .background(Color.Black)
        ) {

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.07f)
                        .background(Color.Black)
                ) {}
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.93f)
                        .background(Color.Black)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.25f)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            val textLegend1 = rememberTextMeasurer()
                            Legend(text = textLegend1, signal = signals[0], Color.Green)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.25f)
                                .background(Color.Black)
                        ) {
                            val textLegend2 = rememberTextMeasurer()
                            Legend(text = textLegend2, signal = signals[1], Color.Cyan)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.25f)
                                .background(Color.Black)
                        ) {
                            val textLegend3 = rememberTextMeasurer()
                            Legend(text = textLegend3, signal = signals[2], Color.Red)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.25f)
                                .background(Color.Black)
                        ) {
                            val textLegend4 = rememberTextMeasurer()
                            Legend(text = textLegend4, signal = signals[3], Color.Yellow)
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.9f)
                .background(Color.Black)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.07f)
                        .background(Color.Black)
                ) {

                    val numberMeasurer = rememberTextMeasurer()
                    var textToDraw = ""

                    Canvas(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {

                        val verticalLines = 6
                        val verticalSize = size.width / (verticalLines + 1)

                        rotate(degrees = 90F, pivot = Offset(0F, 0F)) {
                            drawText(
                                textMeasurer = numberMeasurer,
                                text = axes[0],
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White,
                                ),
                                topLeft = Offset(
                                    x = 10F,
                                    y = (-verticalSize / 2) - 30F
                                )
                            )
                        }

                        repeat(verticalLines) { i ->
                            when(i){
                                0 -> {textToDraw = axes[1]}
                                1 -> {textToDraw = axes[2]}
                                2 -> {textToDraw = axes[3]}
                                3 -> {textToDraw = axes[4]}
                                4 -> {textToDraw = axes[5]}
                                5 -> {textToDraw = axes[6]}
                            }
                            val startX = verticalSize * (i + 1) + (verticalSize / 2)
                            rotate(degrees = 90F, pivot = Offset(0F, 0F)) {
                                drawText(
                                    textMeasurer = numberMeasurer,
                                    text = textToDraw,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        color = Color.White,
                                    ),
                                    topLeft = Offset(
                                        x = 10F,
                                        y = -startX - 30F
                                    )
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.93f)
                        .background(Color.Black)
                ) {

                    Canvas(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {

                        val bar = 1.dp.toPx()
                        val verticalLines = 6
                        val verticalSize = size.width / (verticalLines + 1)

                        drawRect(
                            color = rectColor,
                            style = Stroke(bar),
                            size = Size(size.width, size.height),
                        )

                        drawLine(
                            color = Color.White,
                            start = Offset(verticalSize / 2, 0F),
                            end = Offset(verticalSize / 2, size.height),
                            strokeWidth = bar
                        )

                        repeat(verticalLines) { i ->
                            val startX = verticalSize * (i + 1) + (verticalSize / 2)
                            drawLine(
                                color = Color.White,
                                start = Offset(startX, 0f),
                                end = Offset(startX, size.height),
                                strokeWidth = bar
                            )
                        }
                    }
                    for (i in 0 until signals.size) {
                        val pair = dataViewModel.idxDataSelected[i]
                        if (pair[2] != -1) {
                            CheckSignal(dataViewModel, pair[0], pair[1], pair[2], signalColors[i])
                        }
                    }
                }
            }
        }
    }
}