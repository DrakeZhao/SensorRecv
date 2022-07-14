package com.zz19u21.sensorrecv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zz19u21.sensorrecv.databinding.ActivityDeviceBinding
import com.zz19u21.sensorrecv.databinding.ActivityMainBinding

class DeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.topAppBar.setNavigationOnClickListener{
            finish()
        }
        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId){
                R.id.edit_device ->{
                    Toast.makeText(this, "Edit device", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete_device ->{
                    Toast.makeText(this, "Delete device", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.setDevice ->{
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
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
}