package com.example.blegarden

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData

class Scanner(context: Context) {
    val data = MutableLiveData<MutableList<BTDevice>>()
    val list: MutableList<BluetoothDevice> = ArrayList()
    val data1: MutableList<BTDevice> = ArrayList()

    val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter?.bluetoothLeScanner
    }
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                Log.i(
                    "BluetoothLog",
                    "Scan result - Name: ${name ?: "null"}, address: $address"
                )
                if (!list.contains(result.device)) {
                    list.add(result.device)
                    data1.add(
                        BTDevice(
                            result.device.name ?: "No name",
                            result.device.address,
                            result.device
                        )
                    )
                    data.value = data1
                    Log.i("BluetoothLog", "${data.value?.size.toString()} ${data1.size}")
                }
            }
        }
    }

    fun connectBleDevice(device: BluetoothDevice, context: Context) {
        GattClient.connectGatt(device, context)
        stopBleScan()
    }

    fun startBleScan() {
        bleScanner?.startScan(null, scanSettings, scanCallback)
        list.clear()
        data1.clear()
        data.value?.clear()
    }

    fun stopBleScan() = bleScanner?.stopScan(scanCallback)
}