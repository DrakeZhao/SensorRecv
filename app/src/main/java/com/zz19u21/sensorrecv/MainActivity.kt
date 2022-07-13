package com.zz19u21.sensorrecv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zz19u21.sensorrecv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}