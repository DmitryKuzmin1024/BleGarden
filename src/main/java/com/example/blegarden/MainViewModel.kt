package com.example.blegarden

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blegarden.enums.BytesValue

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mScanner = Scanner(getApplication<Application>().applicationContext)
    val bleData: MutableLiveData<ByteArray> = GattClient.data

    fun setStartButton(dataArray: ByteArray) {
        if (dataArray.size != 8) {
            if (mScanner.bluetoothAdapter.isEnabled) {
                mScanner.startBleScan()
            } else {
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Enable bluetooth",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            GattClient.disconnectGatt()
        }
    }

    fun setDevice(dataArray: ByteArray, positionInArray: Int, turnON: Byte, turnOff: Byte) {
        if (dataArray[positionInArray] == 0.toByte()) {
            GattClient.sendData(turnON)
        } else {
            GattClient.sendData(turnOff)
        }
    }

    fun setKillAllButton() = GattClient.sendData(BytesValue.KILL_ALL.value)

    fun modeDevice(int: Int) = GattClient.sendData(int.toByte())


}