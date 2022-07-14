package com.zz19u21.sensorrecv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.zz19u21.sensorrecv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.card1.setOnClickListener(this)
        binding.card2.setOnClickListener(this)
        binding.card3.setOnClickListener(this)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.add_bluetooth -> {
                    Toast.makeText(this, "Scan bluetooth", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.card1 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                startActivity(intent)
            }
            R.id.card2 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                startActivity(intent)
            }
            R.id.card3 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                startActivity(intent)
            }
        }
    }


}