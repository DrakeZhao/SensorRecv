package com.zz19u21.sensorrecv

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zz19u21.sensorrecv.databinding.ActivityMainBinding
import fontsliderbar.FontSliderBar
import fontsliderbar.FontSliderBar.OnSliderBarChangeListener
import kotlin.properties.Delegates


class MainActivity : BaseActivity() , View.OnClickListener{

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_LOCATION = 101

    private val TAG = "debug"

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"
        val EXTRA_DEVICENAME: String = "Device_name"
    }

    private var textSizef by Delegates.notNull<Float>()
    private var currentIndex by Delegates.notNull<Int>()
    private var isClickable = true
    private var textsize1 = 0f
    private var textsize2 = 0f
    private var textsize3 = 0f
    private var textsize4 = 0f
    private var textsize5 = 0f
    private var textsize6 = 0f
    private var textsize7 = 0f
    private var textsize8 = 0f
    private var textsize9 = 0f
    private var textsize10 = 0f
    private var textsize11 = 0f
    private var textsize12 = 0f
    lateinit var fontSliderBar: FontSliderBar

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.card1.setOnClickListener(this)
        binding.card2.setOnClickListener(this)
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        m_bluetoothAdapter = bluetoothManager.getAdapter()
        if (m_bluetoothAdapter == null){
            Toast.makeText(this, "no bluetooth supported", Toast.LENGTH_SHORT).show()
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions.launch(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            else{
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBluetoothIntent)
            }
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.add_bluetooth -> {
                    checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", REQUEST_ENABLE_LOCATION)
                    Toast.makeText(this, "Scan bluetooth", Toast.LENGTH_SHORT).show()
                    pairedDeviceList()
                    true
                }
                else -> false
            }
        }
        initData()

    }
    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            if(m_bluetoothAdapter!!.isEnabled){
                Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
            }
        }else{
            //deny
            Toast.makeText(this, "Bluetooth has been cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }
    private fun initData() {
        //get the current font index
        currentIndex =
            MyApplication.myInstance!!.preferencesHelper!!.getValueInt("currentIndex", 1)
        Log.d("TAG", "initData lala: $currentIndex")
        textSizef = 1 + currentIndex * 0.1f
        textsize1 = binding.text1.getTextSize() / textSizef
        textsize4 = binding.text4.getTextSize() / textSizef
        val dialogView = LayoutInflater.from(this).inflate(R.layout.text_size_dialog, null)
        fontSliderBar = dialogView.findViewById<FontSliderBar>(R.id.fontSliderBar)
        //add operation when clicking on the font icon
        binding.topAppBar.setNavigationOnClickListener{
            //set dialog
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.text_size_dialog, null)
            materialAlertDialogBuilder.setView(dialogView)
            fontSliderBar = dialogView.findViewById<FontSliderBar>(R.id.fontSliderBar)
            currentIndex =
                MyApplication.myInstance!!.preferencesHelper!!.getValueInt("currentIndex", 1)
            fontSliderBar.setTickCount(6)
                .setTickHeight(DisplayUtils.convertDip2Px(this, 15).toFloat()).setBarColor(Color.GRAY)
                .setTextColor(Color.BLACK)
                .setTextPadding(DisplayUtils.convertDip2Px(this, 10))
                .setTextSize(DisplayUtils.convertDip2Px(this, 14))
                .setThumbRadius(DisplayUtils.convertDip2Px(this, 10).toFloat())
                .setThumbColorNormal(Color.GRAY)
                .setThumbColorPressed(Color.GRAY)
                .setOnSliderBarChangeListener(OnSliderBarChangeListener { rangeBar, index ->
                    var index = index
                    if (index > 5) {
                        return@OnSliderBarChangeListener;
                    }
                    index = index - 2
                    val textSizef = 1 + index * 0.1f
                    setTextSize(textSizef)
                }).setThumbIndex(currentIndex).withAnimation(false).applay()
            materialAlertDialogBuilder.setPositiveButton("Finish"){dialog, _ ->
                dialog.dismiss()
            }
            materialAlertDialogBuilder
                .setTitle("Change Font Size")
                .setIcon(R.drawable.format_size)
                .show()
        }

    }


    private fun checkForPermissions(permission:String, name:String, requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED ->{
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK"){dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String){
            if (grantResults.isNotEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }
        when(requestCode){
            REQUEST_ENABLE_LOCATION -> innerCheck("location")
        }
    }


    private fun pairedDeviceList(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions.launch(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT))
            }
            else{
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
            Log.d("debughere", "pairedDeviceList: 123 return")
        }
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        m_bluetoothAdapter = bluetoothManager.getAdapter()
        if (m_bluetoothAdapter == null){
            Toast.makeText(this, "no bluetooth supported", Toast.LENGTH_SHORT).show()
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions.launch(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT))
            }
            else{
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBluetoothIntent)
            }
        }

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            Log.d("debug", "pairedDeviceList: gps not enabled")
        }
        m_bluetoothAdapter!!.startDiscovery()
        Log.d("debug", "pairedDeviceList: adapter is" + m_bluetoothAdapter)
        Log.d("debug", "pairedDeviceList: startdiscover")
        val list: ArrayList<BluetoothDevice> = ArrayList()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action: String? = intent.action
                Log.d("debug", "onReceive: actionfound" + action)
                when(action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        Log.d("debug", "onReceive: devicefound")
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {

                            Log.d("debughere", "pairedDeviceList: 123 haha")
                        }
//                        val deviceName = device?.name
//                        val deviceHardwareAddress = device?.address // MAC address

                        Log.d("debughere", "pairedDeviceList: 123" + list)

                        if(!list.contains(device)){
                            if (device != null) {
                                if(device.name != null)
                                {
                                    if(device.name.contains("Wave"))
                                        list.add(device)
                                }
                            }
                        }
                        binding.selectDeviceList.isClickable = true
                        binding.selectDeviceList.adapter = MyAdapter(context, list)
                        Log.d(TAG, "onReceive: list adapter is" + binding.selectDeviceList.adapter)

                        binding.selectDeviceList.setOnItemClickListener {parent, view, position, id ->
                            Log.d(TAG, "onReceive: clicked device")
                            val bdevice: BluetoothDevice = list[position]
                            val address: String = bdevice.address
                            val deviceName: String = bdevice.name

                            val tintent = Intent(context, DeviceActivity::class.java)
                            tintent.putExtra(EXTRA_ADDRESS, address)
                            tintent.putExtra(EXTRA_DEVICENAME, deviceName)
                            startActivity(tintent)
                        }
                    }
                }
            }
        }

        Log.d("debug", "pairedDeviceList: here ended")
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter)
        m_bluetoothAdapter!!.startDiscovery()
        Log.d("debug", "pairedDeviceList: discovering?" + m_bluetoothAdapter!!.isDiscovering)
        if (m_bluetoothAdapter!!.isDiscovering()) {
            // bluetooth has started discovery
            Log.d("debug", "pairedDeviceList: isdiscovering")

        }

//        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
//        val list: ArrayList<BluetoothDevice> = ArrayList()
//        val listName: ArrayList<String> = ArrayList()
//        Log.d("debughere", "pairedDeviceList: 123" + m_pairedDevices)
//
//        if(!m_pairedDevices.isEmpty()){
//            for (device: BluetoothDevice in m_pairedDevices){
//                list.add(device)
//                listName.add(device.name)
//                Log.i("device", "pairedDeviceList: " + device)
//            }
//        }else{
//            Toast.makeText(this, "no paired devices found yet", Toast.LENGTH_SHORT).show()
//        }
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listName)
//        binding.selectDeviceList.adapter = adapter
//        binding.selectDeviceList.onItemClickListener = AdapterView.OnItemClickListener{_, _, position, _ ->
//            val device: BluetoothDevice = list[position]
//            val address: String = device.address
//
//            val intent = Intent(this, DeviceActivity::class.java)
//            intent.putExtra(EXTRA_ADDRESS, address)
//            startActivity(intent)
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
//            if(resultCode == Activity.RESULT_OK){
//                if(m_bluetoothAdapter!!.isEnabled){
//                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
//                }
//            } else if(resultCode == Activity.RESULT_CANCELED){
//                Toast.makeText(this, "Bluetooth has been cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun setTextSize(textSize: Float) {
        //Change text size of current page
        currentIndex =
            MyApplication.myInstance!!.preferencesHelper!!.getValueInt("currentIndex", 1)
        binding.text1.setTextSize(DisplayUtils.px2sp(this, textsize1 * textSize).toFloat())
        binding.text4.setTextSize(DisplayUtils.px2sp(this, textsize4 * textSize).toFloat())
        if (currentIndex != fontSliderBar.getCurrentIndex()) {
            if (isClickable) {
                isClickable = false
                refresh()
            }
        }
    }

    private fun refresh() {
        //index of the slider
        MyApplication.myInstance!!.preferencesHelper!!.setValue(
            "currentIndex",
            fontSliderBar.getCurrentIndex()
        )
        //Call the homepage to reload
        RxBus.getInstance()
            .post(DataActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        RxBus.getInstance()
            .post(DataActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        isClickable = true
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.card1 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                    if (isClickable) {
                        isClickable = false
                        refresh()
                    }

                startActivity(intent)
            }
            R.id.card2 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                if (currentIndex != fontSliderBar.getCurrentIndex()) {
                    if (isClickable) {
                        isClickable = false
                        refresh()
                    }
                }
                startActivity(intent)
            }
        }
    }

    override fun rxBusCall(message: MessageSocket?) {
        super.rxBusCall(message)
        when (message!!.id) {
            98 -> {
                //           this.recreate();
                val intent = intent
                overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }
    }

}