package com.biomechApp.imuCap.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvatarViewModel: ViewModel() {

    private val _unityStart = MutableLiveData<Boolean>()
    val unityStart : LiveData<Boolean> = _unityStart
    fun onUnityStartChange(unityStart:Boolean){
        _unityStart.postValue(unityStart)
    }
}