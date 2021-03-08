package com.example.blegarden

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val LED: String = "LED"
    val FUN: String = "FUN"
    val CMP: String = "CMP"

    private val mScanner = Scanner(getApplication<Application>().applicationContext)

    private val mGattClient = GattClient.instance

    val bleData: MutableLiveData<ByteArray> = mGattClient.data

    fun setStartButton(dataArray: ByteArray) {
        if (dataArray.size != 8) {
            mScanner.startBleScan()
        } else {
            mGattClient.disconnectGatt()
        }
    }

    fun setDevice(dataArray: ByteArray, positionInArray: Int, turnON: Byte, turnOff: Byte) {
        if (dataArray[positionInArray] == 0.toByte()) {
            mGattClient.sendData(turnON)
        } else {
            mGattClient.sendData(turnOff.toByte())
        }
    }

    fun setKillAllButton(){
        mGattClient.sendData(BytesValue.KILL_ALL.value)
    }

    fun modeDevice(int: Int) {
        mGattClient.sendData(int.toByte())
    }



}