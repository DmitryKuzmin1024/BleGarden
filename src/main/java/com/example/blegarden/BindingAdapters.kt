package com.example.blegarden

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.example.blegarden.enums.PositionsInArray

@BindingAdapter(value = ["app:onOrOff", "app:intOfArray", "app:deviceName"])
fun onOrOff(button: Button, byteArray: ByteArray, positionInArray: Int, deviceName: String) {
    button.text =
        if (byteArray[positionInArray] == 0.toByte()) "$deviceName OFF" else "$deviceName ON"
}

@BindingAdapter("app:startOrStop")
fun startOrStop(button: Button, dataArray: ByteArray) {
    button.text = if (dataArray.size == 8) "STOP" else "START"
}

@BindingAdapter("app:visibleOrZero")
fun visibleOrZero(view: View, dataArray: ByteArray) {
    view.visibility = if (dataArray.size == 8) View.VISIBLE else View.INVISIBLE
}


@BindingAdapter(value = ["app:setMode", "app:modeStatus"])
fun setMode(radioButton: RadioButton, dataArray: ByteArray, mode: Byte) {
    radioButton.isChecked = dataArray[PositionsInArray.MODE_STATUS.position] == mode
}