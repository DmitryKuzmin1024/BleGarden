package com.example.blegarden

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.*

class GattClient() {

    companion object {
        val instance = GattClient()
        private var gatt1: BluetoothGatt? = null
        private var char1: BluetoothGattCharacteristic? = null
    }

    val data = MutableLiveData<ByteArray>().default(byteArrayOf(0, 0, 0, 0))
    val temp = MutableLiveData<String>()
    val humd = MutableLiveData<String>()
    val ph = MutableLiveData<String>()

    fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

    private val gattCallback = object : BluetoothGattCallback() {

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

            data.postValue(char1?.value!!)

            temp.postValue("t " + char1?.value!![6].toString() + "`c")
            humd.postValue("h " + char1?.value!![7].toString() + "%")
            if (char1?.value!![5].toString().length < 2) {
                ph.postValue("ph " + char1?.value!![4].toString() + ".0" + char1?.value!![5].toString())
            } else {
                ph.postValue("ph " + char1?.value!![4].toString() + "." + char1?.value!![5].toString())
            }

            Log.i(
                "ScanCallback",
                "${char1?.value?.joinToString(" ")} size: ${characteristic.value.size}"
            )
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

    }

    fun connectGatt(devise: BluetoothDevice, context: Context) {
        devise.connectGatt(context, true, gattCallback)
    }

    fun disconnectGatt() {
        data.postValue(byteArrayOf(0, 0, 0, 0))
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