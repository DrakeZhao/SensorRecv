package com.zz19u21.sensorrecv

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.zz19u21.sensorrecv.databinding.ActivityDeviceBinding
import java.io.IOException
import java.util.*

class DeviceActivity :  AppCompatActivity(), View.OnClickListener {
    //debug use
    private val TAG = "debug"

    //bluetooth connection information
    companion object {
        //channel ID
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
        lateinit var m_deviceName: String

    }

    //view binding
    private lateinit var binding: ActivityDeviceBinding
    private var isEdit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)

        //get intent from MainActivity, if no intent has passed, meaning it is the demo activity
        if(intent.hasExtra(MainActivity.EXTRA_DEVICENAME)){
            //if intent has passed data, initialise them
            m_deviceName = intent.getStringExtra(MainActivity.EXTRA_DEVICENAME).toString()
            m_address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS).toString()
            Log.d(TAG, "onCreate: m address is:"+ m_address)
            binding.deviceName.text = m_deviceName

            ConnectToDevice(this).execute()

        }
        val view = binding.root
        setContentView(view)
        binding.sensor1.setOnClickListener(this)
        binding.sensor2.setOnClickListener(this)
        binding.sensor3.setOnClickListener(this)
        //back button
        binding.topAppBar.setNavigationOnClickListener{
            finish()
        }

        //load the rest of the view
        loadData()
        binding.dropdownComment.setTitleText("Comment")
        val ed_text = binding.dropdownComment.getContentTextView()
        //line count function, used runnable
        binding.dropdownComment.getContentTextView().post( Runnable {
            kotlin.run {
                val linecount = binding.dropdownComment.getContentTextView().lineCount
                binding.dropdownComment.setTitleText("Comment" + " (" + linecount + " lines)")
            }
        })

        //check if the comment is empty or not, if empty, hide it
        if(ed_text.length() == 0 || ed_text.equals(""))
        {
            binding.dropdownComment.setVisibility(View.GONE);
        } else {
            binding.dropdownComment.setVisibility(View.VISIBLE);
        }


        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId){
                R.id.edit_device ->{
                    if(!isEdit){
                        //switch icon
                        menuItem.setIcon(R.drawable.done)
                        binding.commentEdit.visibility = View.VISIBLE
                        if (binding.dropdownComment.getContentTextView().text.isNotEmpty()){
                            binding.commentEdit.setText(binding.dropdownComment.getContentTextView().text)
                        }
                        binding.dropdownComment.visibility = View.GONE;
                    }else{
                        menuItem.setIcon(R.drawable.edit)
                        closeKeyboard(binding.commentEdit)
                        binding.commentEdit.visibility = View.GONE
                        saveData()
                        if(ed_text.length() == 0 || ed_text.equals(""))
                        {
                            binding.dropdownComment.setVisibility(View.GONE);
                        } else {
                            binding.dropdownComment.setVisibility(View.VISIBLE);
                        }
                        binding.dropdownComment.getContentTextView().post( Runnable {
                            kotlin.run {
                                val linecount = binding.dropdownComment.getContentTextView().lineCount
                                binding.dropdownComment.setTitleText("Comment" + " (" + linecount + " lines)")
                            }
                        })
                    }
                    isEdit = !isEdit
                    Toast.makeText(this, "Edit device", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete_device ->{
                    Toast.makeText(this, "Delete device", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.setDevice ->{
                    //jump to editor activity
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    val deviceName = binding.deviceName.text.toString()
                    val intent = Intent(this, EditorActivity::class.java)
                    intent.putExtra("DeviceName", deviceName)
                    startActivity(intent)
                    true
                }
                R.id.exportData -> {
                    Toast.makeText(this, "Export Data", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }



    }


    //bluetooth related activity
    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>(){
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        //background connect bluetooth
        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }

    }


    //set close keyboard when not commenting
    private fun closeKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loadData() {
        //get stored comment content
        val sharedPreferences = getSharedPreferences(binding.deviceName.text.toString(), Context.MODE_PRIVATE)
        //if nothing is passed, set to empty
        val savedString = sharedPreferences.getString("COMMENT_KEY", "")
        if (savedString != null) {
            binding.dropdownComment.setContentText(savedString)
        }else{
            binding.dropdownComment.setContentText("")
        }
    }

    private fun saveData() {
        //save the comment to shared preferences
        val deviceName = binding.deviceName.text.toString()
        val insertedText = binding.commentEdit.text.toString()
        val sharedPreferences = getSharedPreferences(binding.deviceName.text.toString(), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply(){
            putString("COMMENT_KEY", insertedText)
        }.apply()
        binding.dropdownComment.setContentText(insertedText)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.sensor1 -> {
                //jump to data activity
                val intent = Intent(this, DataActivity::class.java)
                startActivity(intent)
            }
            R.id.sensor2 -> {
                val intent = Intent(this, DataActivity::class.java)
                startActivity(intent)
            }
            R.id.sensor3 -> {
                val intent = Intent(this, DataActivity::class.java)
                startActivity(intent)
            }
        }
    }
//    override fun rxBusCall(message: MessageSocket?) {
//        super.rxBusCall(message)
//        when (message!!.id) {
//            98 -> {
//                //           this.recreate();
//                val intent = intent
//                overridePendingTransition(0, 0)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                finish()
//                overridePendingTransition(0, 0)
//                startActivity(intent)
//            }
//        }
//    }
}