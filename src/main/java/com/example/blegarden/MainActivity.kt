package com.example.blegarden

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mVm by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java)}

        promptEnableBluetooth()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val mScanner = Scanner(this)
        val mGattClient = Gatt()

        scan_button.setOnClickListener { mScanner.startBleScan() }

        buttonKill.setOnClickListener { mGattClient.sendData(0) }

        radio_red1.setOnClickListener { mGattClient.sendData(8) }
        radio_red2.setOnClickListener { mGattClient.sendData(9) }
        radio_red0.setOnClickListener { mGattClient.sendData(7) }

//        mVm.getBleData().observe(this, Observer {
//            Log.i(
//                "ScanCallback",
//                "$it"  + "rrr "      )
//        })

        mVm.getBleData().observe(this, Observer { ///!!!!!!!!!!!! nvg

            if (it != null) {

                val data = it.split(" ")

                scan_button.text = "Disconnect"
                scan_button.setOnClickListener { mGattClient.disconnectGatt() }
                radio_group.visibility = View.VISIBLE

                if (it.length > 2) {
                    textView.text = "t ${data[2].replace(".00", "")}`C"
                    textView1.text = "h ${data[3].replace(".0", "")}%"
                    textView2.text = "ph ${data[1]}"
                }

                when (data[0][0].toString()) {
                    "1" -> {
                        buttonWrite.text = "LED on"
                        buttonWrite.setOnClickListener { mGattClient.sendData(2) }
                    }
                    "0" -> {
                        buttonWrite.text = "LED off"
                        buttonWrite.setOnClickListener { mGattClient.sendData(1) }
                    }
                }
                when (data[0][1].toString()) {
                    "1" -> {
                        buttonWrite2.text = "FAN on"
                        buttonWrite2.setOnClickListener { mGattClient.sendData(4) }
                    }
                    "0" -> {
                        buttonWrite2.text = "FAN off"
                        buttonWrite2.setOnClickListener { mGattClient.sendData(3) }
                    }
                }
                when (data[0][2].toString()) {
                    "1" -> {
                        buttonWrite3.text = "CMP on"
                        buttonWrite3.setOnClickListener { mGattClient.sendData(6) }
                    }
                    "0" -> {
                        buttonWrite3.text = "CMP off"
                        buttonWrite3.setOnClickListener { mGattClient.sendData(5) }
                    }
                }
                when (data[0][3].toString()) {
                    "0" -> radio_red0.isChecked = true
                    "1" -> radio_red1.isChecked = true
                    "2" -> radio_red2.isChecked = true
                }

            } else {
                buttonWrite.text = "LED"
                buttonWrite2.text = "FAN"
                buttonWrite3.text = "CMP"
                scan_button.text = "Connect"
                scan_button.setOnClickListener { mScanner.startBleScan() }
                textView.text = "NO"
                textView1.text = "DATA"
                textView2.text = ""
                radio_group.visibility = View.INVISIBLE
            }

        })

//            promptEnableBluetooth()

        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this,
                    "The permission to get BLE location data is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), 1
                    )
                }
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun promptEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
    }

}