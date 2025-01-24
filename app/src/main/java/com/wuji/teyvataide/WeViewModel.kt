package com.wuji.teyvataide

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WeViewModel:ViewModel() {
    var selectTab by mutableStateOf(0)
}