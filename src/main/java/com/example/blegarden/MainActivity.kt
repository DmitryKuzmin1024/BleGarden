package com.example.blegarden

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val zero: Byte = 0
    private val one: Byte = 1
    private val two: Byte = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mVm by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

        promptEnableBluetooth()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        mVm.setStartButton(scan_button)

        mVm.setButton(radio_red0, 7)
        mVm.setButton(radio_red1, 8)
        mVm.setButton(radio_red2, 9)
        mVm.setButton(buttonKill, 0)

        mVm.bleData.observe(this, Observer {

            if (it.size == 8) {

                mVm.setText(
                    textView,
                    it[6].toString(),
                    textView1,
                    it[7].toString(),
                    textView2,
                    it[4].toString(),
                    it[5].toString()
                )

                radio_group.visibility = View.VISIBLE

                mVm.setSopButton(scan_button)

                when (it[0]) {
                    zero -> mVm.setButton(buttonWrite, 1, "LED off")
                    one -> mVm.setButton(buttonWrite, 2, "LED on")
                }
                when (it[1]) {
                    zero -> mVm.setButton(buttonWrite2, 3, "FAN off")
                    one -> mVm.setButton(buttonWrite2, 4, "FAN on")
                }
                when (it[2]) {
                    zero -> mVm.setButton(buttonWrite3, 5, "CMP off")
                    one -> mVm.setButton(buttonWrite3, 6, "CMP on")
                }
                when (it[3]) {
                    zero -> radio_red0.isChecked = true
                    one -> radio_red1.isChecked = true
                    two -> radio_red2.isChecked = true
                }

            } else {
                buttonWrite.text = "LED"
                buttonWrite2.text = "FAN"
                buttonWrite3.text = "CMP"

                textView.text = "NO"
                textView1.text = "DATA"
                textView2.text = ""

                radio_group.visibility = View.INVISIBLE

                mVm.setStartButton(scan_button)

            }

        })

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