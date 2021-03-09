package com.example.blegarden

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["app:onOrOff", "app:intOfArray", "app:deviceName"])
fun onOrOff(button: Button, byteArray: ByteArray, int: Int, deviceName: String) {
    button.text = if (byteArray[int] == 0.toByte()) "$deviceName OFF" else "$deviceName ON"
}

@BindingAdapter("app:startOrStop")
fun startOrStop(button: Button, byteArray: ByteArray) {
    button.text = if (byteArray.size == 8) "STOP" else "START"
}

@BindingAdapter("app:VisibleOrZero")
fun VisibleOrZero(view: View, byteArray: ByteArray) {
    view.visibility = if (byteArray.size == 8) View.VISIBLE else View.INVISIBLE
}


@BindingAdapter(value = ["app:setMode", "app:modeStatus"])
fun setMode(radioButton: RadioButton, byteArray: ByteArray, int: Int) {
    radioButton.isChecked = byteArray[3] == int.toByte()
}