package com.example.blegarden.mainFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blegarden.GattClient
import com.example.blegarden.enums.BytesValue

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val bleData: MutableLiveData<ByteArray> = GattClient.data

    fun setDevice(dataArray: ByteArray, positionInArray: Int, turnON: Byte, turnOff: Byte) {
        if (dataArray[positionInArray] == 0.toByte()) {
            GattClient.sendData(turnON)
        } else {
            GattClient.sendData(turnOff)
        }
    }

    fun setTurnOffAllButton() = GattClient.sendData(BytesValue.TURN_OFF_ALL.value)
    fun modeDevice(int: Int) = GattClient.sendData(int.toByte())
}