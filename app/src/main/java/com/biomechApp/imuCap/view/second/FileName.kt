package com.biomechApp.imuCap.view.second

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.biomechApp.imuCap.viewModel.ConfigViewModel

@Composable
fun FileName(configViewModel: ConfigViewModel){

    val fileName: String by configViewModel.recordingFileName.observeAsState(initial = "")
    val fileNameEnable: Boolean by configViewModel.recordingFileNameEnable.observeAsState(initial = true)

    Dialog(
        onDismissRequest = {
            configViewModel.onShowRecordingNameChange(false)
        }
    ) {
        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp, bottom = 0.dp),
                    text = "Recording File Name",
                    fontSize = 20.sp,
                    color = Color.White
                )
                TextField(
                    modifier = Modifier
                        .padding(5.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray,
                        unfocusedContainerColor = Color.Gray,
                        focusedPlaceholderColor = Color.Blue,
                        unfocusedPlaceholderColor = Color.Blue),
                    enabled = fileNameEnable,
                    textStyle = TextStyle(color = Color.Blue),
                    value = fileName,
                    onValueChange = {
                        configViewModel.onRecordingFileNameChange(it)
                    },
                    placeholder = { Text("Enter a file name") }
                )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                )
                Row(modifier = Modifier.padding(start = 160.dp, top = 5.dp)) {
                    TextButton(
                        onClick = {
                            configViewModel.onShowRecordingNameChange(false)
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Blue
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    TextButton(
                        onClick = {
                            configViewModel.onFileOutChange(fileName)
                            configViewModel.onShowRecordingNameChange(false)
                        }
                    ) {
                        Text(
                            text = "Apply",
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    }
}