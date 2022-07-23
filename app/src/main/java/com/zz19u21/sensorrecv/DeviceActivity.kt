package com.zz19u21.sensorrecv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.zz19u21.sensorrecv.databinding.ActivityDeviceBinding

class DeviceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.sensor1.setOnClickListener(this)
        binding.sensor2.setOnClickListener(this)
        binding.sensor3.setOnClickListener(this)
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
        binding.dropdownComment.setTitleText("Comment")
        binding.dropdownComment.setContentText(" This is test commentThis is test comment \nThis is test commentThis is test commentThis is test commentThis is test commentThis is test commentThis is test comment\nThis is test commentThis is test comment")
        val ed_text = binding.dropdownComment.getContentTextView()
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

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.sensor1 -> {
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