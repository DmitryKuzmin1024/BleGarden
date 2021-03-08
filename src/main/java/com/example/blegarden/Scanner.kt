package com.example.blegarden

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log

class Scanner(context: Context) {

    private val mGattClient = GattClient.instance

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .build()

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                Log.i("ScanCallback", "Name: ${name ?: "null"}, address: $address")
                if (address == "A8:10:87:1B:4D:10") {
                    mGattClient.connectGatt(result.device, context)
                    stopBleScan()
                    Log.i("ScanCallback", "Connected")
                }
            }
        }

    }

    fun startBleScan() {
        bleScanner.startScan(null, scanSettings, scanCallback)
    }

    fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
    }

}