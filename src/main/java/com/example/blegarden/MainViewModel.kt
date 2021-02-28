package com.example.blegarden

import android.app.Application
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){

    private companion object {
        private val bleData = MutableLiveData<String>()
    }

    fun getBleData(): LiveData<String?> { return bleData }


    fun setData (str: String){
        bleData.postValue(str)
        Log.i("ScanCallback","${bleData.value}")
    }

}