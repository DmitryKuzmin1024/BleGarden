package com.example.blegarden.scanFragment

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blegarden.R
import com.example.blegarden.Scanner

class ScanFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val mScanner = Scanner(getApplication<Application>().applicationContext)
    val scanData = mScanner.data
    val scanStatus = MutableLiveData<Boolean>(false)

    init {
        scan()
    }

    fun scan() {
        if (mScanner.bluetoothAdapter?.isEnabled == false) {
            scanStatus.value = false
            Toast.makeText(
                getApplication<Application>().applicationContext,
                R.string.Turn_on_bluetooth,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            scanStatus.value = true
            mScanner.startBleScan()
            Log.i("BluetoothLog", "Start scan")
            object : CountDownTimer(5000, 1000) { // точно ли 5 сек
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    mScanner.stopBleScan()
                    Log.i("BluetoothLog", "Stop scan")
                    scanStatus.value = false
                }
            }.start()
        }
    }

    fun connectDevice(device: BluetoothDevice) {
        mScanner.connectBleDevice(
            device,
            getApplication<Application>().applicationContext
        )
        Log.i("BluetoothLog", "${device.address} connecting")
    }
}