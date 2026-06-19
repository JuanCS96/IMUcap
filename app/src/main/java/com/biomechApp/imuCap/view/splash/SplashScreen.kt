package com.biomechApp.imuCap.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.biomechApp.imuCap.R
import com.biomechApp.imuCap.view.navegation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigationController: NavHostController) {

    LaunchedEffect(key1 = true){
        delay(2000)
        navigationController.popBackStack()
        navigationController.navigate(Routes.Screen1.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.padding(5.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "splash")
    }
}