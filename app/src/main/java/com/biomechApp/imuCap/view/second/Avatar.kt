package com.biomechApp.imuCap.view.second

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.biomechApp.imuCap.viewModel.SensorListViewModel

data class Joint(
    val name: String,
    val xRatio: Float,
    val yRatio: Float,
    var selected: Boolean = false
)

@Composable
fun JointTicks(sensorListViewModel: SensorListViewModel) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val imageBitmap = ImageBitmap.imageResource(id = com.biomechApp.imuCap.R.drawable.avatar)
    val imageOriginalWidth = imageBitmap.width
    val imageOriginalHeight = imageBitmap.height

    val joints = sensorListViewModel.jointTicks

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = com.biomechApp.imuCap.R.drawable.avatar),
            contentDescription = "model",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    imageSize = coordinates.size
                }
        )

        if (imageSize.width > 0 && imageSize.height > 0) {
            val scale = minOf(
                imageSize.width.toFloat() / imageOriginalWidth,
                imageSize.height.toFloat() / imageOriginalHeight
            )

            val displayWidth = imageOriginalWidth * scale
            val displayHeight = imageOriginalHeight * scale

            val startX = (imageSize.width - displayWidth) / 2
            val startY = (imageSize.height - displayHeight) / 2

            joints.forEachIndexed { index, joint ->
                val tickX = startX + joint.xRatio * displayWidth
                val tickY = startY + joint.yRatio * displayHeight

                IconButton(
                    onClick = {
                        if (sensorListViewModel.jointSelected.size <= 5 &&
                            sensorListViewModel.recordingType == "null") {
                            sensorListViewModel.toggleJointTicks(joints[index].name)
                            sensorListViewModel.toggleJointSelection(joints[index].name)
                        }
                        else if ((sensorListViewModel.jointSelected.size == 6 && joint.selected) &&
                            sensorListViewModel.recordingType == "null") {
                            sensorListViewModel.toggleJointTicks(joints[index].name)
                            sensorListViewModel.toggleJointSelection(joints[index].name)
                        }
                    },
                    modifier = Modifier
                        .offset(
                            x = with(LocalDensity.current) { tickX.toDp() },
                            y = with(LocalDensity.current) { tickY.toDp() }
                        )
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (joint.selected) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.Circle
                        },
                        contentDescription = joint.name,
                        tint = if (joint.selected) Color.Green else Color.Red
                    )
                }
            }
        }
    }
}
