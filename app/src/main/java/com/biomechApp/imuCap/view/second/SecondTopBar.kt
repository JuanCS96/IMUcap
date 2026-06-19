package com.biomechApp.imuCap.view.second

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.biomechApp.imuCap.R
import com.biomechApp.imuCap.viewModel.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondTopBar(configViewModel: ConfigViewModel){

    val showRecordingName: Boolean by configViewModel.showRecordingName.observeAsState(initial = false)

    TopAppBar(
        title = { Image(modifier = Modifier.size(225.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo")
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black,
            titleContentColor = Color.White),
        actions = {
            IconButton(modifier= Modifier.padding(10.dp),
                onClick = {
                    configViewModel.onShowRecordingNameChange(true)
                }
            ) {
                Icon(imageVector = Icons.TwoTone.Edit,
                    contentDescription = "Csv",
                    tint = Color.White)
            }
        }
    )
    if (showRecordingName){
        configViewModel.onRecordingFileNameChange(configViewModel.fileOutName)
        FileName(configViewModel)
    }
}