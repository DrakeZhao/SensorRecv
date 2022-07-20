package com.zz19u21.sensorrecv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zz19u21.sensorrecv.databinding.ActivityDataBinding

class DataActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.topAppBar.setNavigationOnClickListener{
            finish()
        }
        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId){
                R.id.shareValue ->{
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.setDevice ->{
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete_device->{
                    Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }

        }
    }


}