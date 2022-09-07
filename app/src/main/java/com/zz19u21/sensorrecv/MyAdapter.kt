package com.zz19u21.sensorrecv

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat

class MyAdapter (context: Context, private val arrayList: ArrayList<BluetoothDevice>) : ArrayAdapter<BluetoothDevice>(context,
    R.layout.card_cell, arrayList) {

    private val TAG = "debug adapter"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_cell, null)

        val deviceName: TextView = view.findViewById(R.id.bluetooth_device)
        val deviceAddress: TextView = view.findViewById(R.id.bluetooth_address)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        deviceName.text = arrayList[position].name
        Log.d(TAG, "getView: devicename" + deviceName.text)
        val textAddress = "Bluetooth Address: " + arrayList[position].address
        deviceAddress.text = textAddress
        return view
    }



}