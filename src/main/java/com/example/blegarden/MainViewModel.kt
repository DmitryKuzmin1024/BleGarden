package com.example.blegarden

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val LED: String = "LED"
    val FUN: String = "FUN"
    val CMP: String = "CMP"

    val mScanner = Scanner(getApplication<Application>().applicationContext)

    private val mGattClient = GattClient.instance

    val bleData: MutableLiveData<ByteArray> = mGattClient.data

    var temperature: MutableLiveData<String> = mGattClient.temp
    var humidity: MutableLiveData<String> = mGattClient.humd
    var ph: MutableLiveData<String> = mGattClient.ph

    fun setStartButton() {
        if (bleData.value?.size != 8) {
            mScanner.startBleScan()
        } else {
            mGattClient.disconnectGatt()
        }
    }

    fun setDevice(int: Int, int1: Int, int2: Int) {
        if (bleData.value!![int] == 0.toByte()) {
            mGattClient.sendData(int1.toByte())
        } else {
            mGattClient.sendData(int2.toByte())
        }
    }

    fun modeDevice(int: Int) {
        mGattClient.sendData(int.toByte())
    }

}