package com.example.blegarden

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Gatt : BluetoothGattCallback() {

    val mMainViewModel = MainViewModel()

    private companion object {
//        private val bleData = MutableLiveData<String>()
        private var gatt1: BluetoothGatt? = null
        private var char1: BluetoothGattCharacteristic? = null
    }

//    fun getBleData(): LiveData<String?> { return bleData }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        gatt1 = gatt.also { it.discoverServices() }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        Log.i(
            "ScanCallback",
            "Discovered ${gatt.services.size} services for ${gatt.device.address}"
        )
        gatt.services.firstOrNull { service ->
            service.uuid.toString() == "0000ffe0-0000-1000-8000-00805f9b34fb"
        }
            ?.characteristics?.firstOrNull { char ->
                char.uuid.toString() == "0000ffe1-0000-1000-8000-00805f9b34fb"
            }
                ?.let { myChar ->
                    char1 = myChar.also { gatt1?.readCharacteristic(it) }
                }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            gatt1?.setCharacteristicNotification(char1, true)
            Log.i(
                "ScanCallback",
                "Read char ${char1?.value?.map { String.format("%02x", it) }}"
            )
        }
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        char1?.value?.map { it.toChar() }.let {
            if (it?.size == 20) {
//                bleData.postValue(it?.joinToString(""))
                mMainViewModel.setData(it.joinToString ( "" ))
//                Log.i(
//                    "ScanCallback",
//                    "converted: ${ bleData.value?.replace("\n", " ")} size: ${characteristic.value.size}"
//                )
            }
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        super.onCharacteristicWrite(gatt, characteristic, status)
        Log.i(
            "ScanCallback",
            "Writing ${Arrays.toString(char1?.value)}"
        )
    }

    fun connectGatt(devise: BluetoothDevice, context: Context) {
        devise.connectGatt(context, true, this)
    }

    fun disconnectGatt() {
//        bleData.postValue(null)
        gatt1?.disconnect()
        gatt1?.close()
    }

    fun readData() {
        gatt1?.readCharacteristic(char1)
    }

    fun sendData(data: Byte) {
        char1?.value = byteArrayOf(data)
        gatt1?.writeCharacteristic(char1)
    }

}