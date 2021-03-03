package com.example.blegarden

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["app:onOrOff", "app:intOfArray", "app:deviceName"])
fun onOrOff(button: Button, byteArray: ByteArray, int: Int, deviceName: String) {
    if (byteArray[int] == 0.toByte()) {
        button.text = "$deviceName OFF"
    } else {
        button.text = "$deviceName ON"
    }
}

@BindingAdapter("app:startOrStop")
fun startOrStop(button: Button, byteArray: ByteArray) {
    if (byteArray.size == 8) {
        button.text = "STOP"
    } else {
        button.text = "START"
    }
}

@BindingAdapter("app:VisibleOrZero")
fun VisibleOrZero(view: View, byteArray: ByteArray) {
    view.visibility = if (byteArray.size == 8) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter(value = ["app:setMode", "app:modeStatus"])
fun setMode(radioButton: RadioButton, byteArray: ByteArray, int: Int) {
    if (byteArray[3] == int.toByte()) {
        radioButton.isChecked = true
    }
}




