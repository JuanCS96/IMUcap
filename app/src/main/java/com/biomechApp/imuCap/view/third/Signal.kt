package com.biomechApp.imuCap.view.third

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.Joint
import com.biomechApp.imuCap.viewModel.JointDoF
import com.biomechApp.imuCap.viewModel.Segment
import com.biomechApp.imuCap.viewModel.SegmentDoF

@Composable
fun CheckSignal(dataViewModel: DataViewModel, elementIdx: Int, dofIdx: Int, joint: Int, signalColor: Color) {

    val livePath = if (joint == 0) {
        dataViewModel.getJointPathLiveData(
            Joint.values()[elementIdx],
            JointDoF.values()[dofIdx]
        )
    } else {
        dataViewModel.getSegmentPathLiveData(
            Segment.values()[elementIdx],
            SegmentDoF.values()[dofIdx]
        )
    }

    val observedPath by livePath.observeAsState(Path())

    if (!observedPath.isEmpty) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {
                        drawPath(
                            path = observedPath,
                            color = signalColor,
                            style = Stroke(width = 4f)
                        )
                    }
                }
        )
    }
}

