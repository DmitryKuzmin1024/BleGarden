package com.example.blegarden

import android.bluetooth.*
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.*

object GattClient {
    private val serviceUuid: UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val charUuid: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private var gatt1: BluetoothGatt? = null
    private var char1: BluetoothGattCharacteristic? = null
    val data = MutableLiveData<ByteArray>(byteArrayOf(0, 0, 0, 0))

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt1 = gatt.also { it.discoverServices() }
            } else {
                data.postValue(byteArrayOf(0, 0, 0, 0))
                gatt1?.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(
                    "BluetoothLog",
                    "Discovered ${gatt.services.size} services for ${gatt.device.address}"
                )
                gatt.services.firstOrNull { service ->
                    service.uuid == serviceUuid
                }
                    ?.characteristics?.firstOrNull { char ->
                        char.uuid == charUuid
                    }
                    ?.let { myChar ->
                        char1 = myChar.also { gatt1?.readCharacteristic(it) }
                    }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (characteristic?.uuid == charUuid) {
                    gatt1?.setCharacteristicNotification(char1, true)
                    Log.i(
                        "BluetoothLog",
                        "Read char ${characteristic.uuid} value ${ characteristic.value.joinToString(" ") }"
                    )
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == charUuid) {
                characteristic.value?.let { data.postValue(it) }
                Log.i(
                    "BluetoothLog",
                    "${characteristic.value?.joinToString(" ")} size: ${characteristic.value.size}"
                )
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (characteristic?.uuid == charUuid) {
                    super.onCharacteristicWrite(gatt, characteristic, status)
                    Log.i(
                        "BluetoothLog",
                        "Writing ${characteristic.value?.joinToString(" ")}"
                    )
                }
            }
        }
    }

    fun connectGatt(devise: BluetoothDevice, context: Context) =
        devise.connectGatt(context, true, gattCallback)

    fun disconnectGatt() = gatt1?.disconnect()


    fun sendData(data: Byte) {
        char1?.value = byteArrayOf(data)
        gatt1?.writeCharacteristic(char1)
    }


}