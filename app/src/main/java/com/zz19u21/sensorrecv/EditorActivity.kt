package com.zz19u21.sensorrecv

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.zz19u21.sensorrecv.databinding.ActivityEditorBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.math.log

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    private val REQ_WRITE_EXTERNAL: Int = 100;
    private val FILE_NAME: String = "Device.yaml"
    private val TAG = "TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnWrite.setOnClickListener{
            val yamlContent:String = binding.editTextTextMultiLine.text.toString()


            val theFileDir:File = createAppDir()
            
            //write file into app dir
            var newFile: File = File(theFileDir, FILE_NAME)

            try {
                FileOutputStream(newFile).use {
                    Log.d(TAG, "onCreate: yamlcontent" + yamlContent)
                    it.write(yamlContent.toByteArray())
                }
            }catch (e: Exception){
                Log.d(TAG, "file didn't write")
            }

            Toast.makeText(this, "File Written", Toast.LENGTH_SHORT).show()
        }

        binding.btnRead.setOnClickListener {
            val theFileDir:File = createAppDir()
            val newFile = File(theFileDir, FILE_NAME)

            try {
                val inputAsString = FileInputStream(newFile).bufferedReader().use { it.readText() }
                binding.editTextTextMultiLine.setText(inputAsString)
            } catch (e: Exception){
                Log.d(TAG, "file didn't read")
            }
        }


        checkRuntimePermission()
    }

    private fun createAppDir(): File {
//        //Create Directory with app name
//        var theFileDir: File = Environment.getExternalStorageDirectory()
//        theFileDir = File(theFileDir, getString(R.string.app_name))
//        Log.d(TAG, "createAppDir: " + theFileDir)
//        if(!theFileDir.exists()){
//            if (theFileDir.mkdirs()){
//                Log.d(TAG, "createAppDir: dir created")
//                Toast.makeText(this,"Dir Created", Toast.LENGTH_SHORT).show()
//            }else{
//                Log.d(TAG, "createAppDir: dir not created")
//                Toast.makeText(this,"Dir not Created", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return theFileDir

        val path = this.getExternalFilesDir(null)
        val theFileDir = File(path, getString(R.string.app_name))

        if(!theFileDir.exists()){
            if (theFileDir.mkdirs()){
                Log.d(TAG, "createAppDir: dir created")
                Toast.makeText(this,"Dir Created", Toast.LENGTH_SHORT).show()
            }else{
                Log.d(TAG, "createAppDir: dir not created")
                Toast.makeText(this,"Dir not Created", Toast.LENGTH_SHORT).show()
            }
        }
        return theFileDir
    }

    private fun checkRuntimePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_WRITE_EXTERNAL)
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
            REQ_WRITE_EXTERNAL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }

    }
}