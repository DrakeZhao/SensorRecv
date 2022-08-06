package com.zz19u21.sensorrecv

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zz19u21.sensorrecv.databinding.ActivityDeviceBinding
import org.billthefarmer.editor.Editor
import java.io.File


class DeviceActivity :  AppCompatActivity(), View.OnClickListener {
    val KITKAT_VALUE = 1002
    private lateinit var binding: ActivityDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        binding.sensor1.setOnClickListener(this)
        binding.sensor2.setOnClickListener(this)
        binding.sensor3.setOnClickListener(this)
        binding.topAppBar.setNavigationOnClickListener{
            finish()
        }
        Log.d("Tag", "onCreate: device layout" + theme)
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

                    // Check permissions




//                    if (Build.VERSION.SDK_INT < 24) {
//                        fileUri = Uri.fromFile(file);
//                    } else {
//                        fileUri = Uri.parse(file.getPath()); // My work-around for SDKs up to 29.
//                    }

//                    Log.d("tag", "onCreate: fileUri:" + uri)

                    askForPermission()


//                    startActivityForResult(intent, Editor.OPEN_DOCUMENT)
//                    sendIntent.setDataAndType(uri, "text/plain")
//                    sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    startActivity(sendIntent)





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

    public fun askForPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), Editor.REQUEST_READ
                )
                return
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            Editor.REQUEST_READ -> {
                var i = 0
                while (i < grantResults.size) {
                    if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
                    ) // Granted, read file{
                    {
                        val documents =
                            File(Environment.getExternalStorageDirectory(), Editor.DOCUMENTS)
                        val file = File(documents, "Editor.txt")
                        Log.d("tag", "onCreate: fileUri:" + file)


                        // Get file provider uri
//                    val uri = FileProvider.getUriForFile(this, Editor.FILE_PROVIDER, file)

                        var fileUri: Uri
                        fileUri = Uri.fromFile(file)
                        val intent: Intent = Intent(Intent.ACTION_SEND)
                        intent.setDataAndType(fileUri, Editor.TEXT_WILD)
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                        startActivity(intent)
                    }
                    i++
                }
            }
        }
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
