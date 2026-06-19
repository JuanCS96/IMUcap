package com.biomechApp.imuCap.view.third

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.QueryStats
import androidx.compose.material.icons.twotone.ViewInAr
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
import com.biomechApp.imuCap.viewModel.AvatarViewModel
import com.biomechApp.imuCap.viewModel.DataViewModel
import com.biomechApp.imuCap.viewModel.PlotViewModel
import com.unity3d.player.UnityPlayerActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdTopBar(plotViewModel: PlotViewModel,
                dataViewModel: DataViewModel,
                avatarViewModel: AvatarViewModel,
                appContext: Context
){

    val showPlotConfig: Boolean by plotViewModel.showPlotConfig.observeAsState(initial = false)

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
                    appContext.startActivity(Intent(appContext, UnityPlayerActivity::class.java))
                    avatarViewModel.onUnityStartChange(true)
                }) {
                Icon(imageVector = Icons.TwoTone.ViewInAr,
                    contentDescription = "Model",
                    tint = Color.White)
            }
            IconButton(modifier= Modifier.padding(10.dp),
                onClick = { plotViewModel.onShowPlotConfigChange(true) }) {
                Icon(imageVector = Icons.TwoTone.QueryStats,
                    contentDescription = "Plot",
                    tint = Color.White)
            }
        }
    )
    if (showPlotConfig){
        PlotConfig(plotViewModel, dataViewModel)
    }
}