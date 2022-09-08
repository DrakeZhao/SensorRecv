package com.zz19u21.sensorrecv

import android.content.Intent
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
        //actions on menu items
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
//notify the change of font size
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