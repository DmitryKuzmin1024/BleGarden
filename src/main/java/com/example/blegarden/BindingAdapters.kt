package com.example.blegarden

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.blegarden.enums.PositionsInArray

@BindingAdapter(value = ["app:onOrOff", "app:intOfArray", "app:deviceName"])
fun onOrOff(button: Button, byteArray: ByteArray, positionInArray: Int, deviceName: String) {
    button.text =
        if (byteArray[positionInArray] == 0.toByte()) "$deviceName OFF" else "$deviceName ON"
}

@BindingAdapter("app:visibleOrZero")
fun visibleOrZero(view: View, dataArray: ByteArray) {
    view.visibility = if (dataArray.size == 8) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:noDataErrorVisibility")
fun noDataErrorVisibility(view: View, dataArray: ByteArray) {
    view.visibility = if (dataArray.size == 6) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:waitingBarVisibility")
fun waitingBarVisibility(view: View, dataArray: ByteArray) {
    view.visibility = if (dataArray.size == 8 || dataArray.size == 6) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(value = ["app:setMode", "app:modeStatus"])
fun setMode(radioButton: RadioButton, dataArray: ByteArray, mode: Byte) {
    radioButton.isChecked = dataArray[PositionsInArray.MODE_STATUS.position] == mode
}

@BindingAdapter(value = ["app:updateLayout"])
fun updateLayout(view: SwipeRefreshLayout, function: () -> Unit) {
    view.setOnRefreshListener {
        function()
    }
}

@BindingAdapter(value = ["app:bluetoothStatus"])
fun startScan(button: Button, bluetoothStatus: Boolean) {
    button.visibility = if (bluetoothStatus) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(value = ["app:refreshing"])
fun refreshing(view: SwipeRefreshLayout, bluetoothStatus: Boolean) {
    view.isRefreshing = bluetoothStatus
}