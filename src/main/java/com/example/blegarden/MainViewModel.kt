package com.example.blegarden

import android.app.Application
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val mScanner = Scanner(getApplication<Application>().applicationContext)

    private val mGattClient = GattClient.instance

    val bleData: MutableLiveData<ByteArray> = mGattClient.data

    fun setStartButton(button: Button) {
        button.text = "Connect"
        button.setOnClickListener { mScanner.startBleScan() }
    }

    fun setSopButton(button: Button) {
        button.text = "Disconnect"
        button.setOnClickListener { mGattClient.disconnectGatt() }
    }

    fun setButton(button: Button, byte: Byte, str: String = button.text.toString()) {
        button.text = str
        button.setOnClickListener { (mGattClient.sendData(byte)) }
    }

    fun setText(
        textView1: TextView,
        str1: String,
        textView2: TextView,
        str2: String,
        textView3: TextView,
        str3: String,
        str4: String
    ) {

        if (str4.length < 2) {
            textView3.text =
                "ph $str3.0$str4"
        } else {
            textView3.text =
                "ph $str3.$str4"
        }
        textView1.text = "t $str1`c"
        textView2.text = "h $str2%"
    }

}