package com.zz19u21.sensorrecv

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zz19u21.sensorrecv.databinding.ActivityEditorBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.regex.Matcher
import java.util.regex.Pattern


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

        binding.editTextTextMultiLine.addTextChangedListener(object : TextWatcher {
            var keywords = ColorScheme(
                Pattern.compile(
                    "\\b(package|transient|strictfp|void|char|short|int|long|double|float|const|static|volatile|byte|boolean|class|interface|native|private|protected|public|final|abstract|synchronized|enum|instanceof|assert|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|super|extends|implements|import|true|false|null)\\b"
                ),
                Color.CYAN
            )
            var numbers = ColorScheme(
                Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)"),
                Color.BLUE
            )

            var comments = ColorScheme(
                Pattern.compile(
                    "(?m)^#.*\\n?"
                ),
                Color.GRAY
            )

            var dquotes = ColorScheme(
                Pattern.compile(
                    "(\")((?:[^\"]|\"\")*)\""
                ),
                Color.GREEN
            )

            var squotes = ColorScheme(
                Pattern.compile(
                    "(')((?:[^']|'')*)'"
                ),
                Color.GREEN
            )

            val schemes = arrayOf(keywords, numbers, comments, dquotes, squotes)
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                removeSpans(s, ForegroundColorSpan::class.java)
                for (scheme in schemes) {
                    val m: Matcher = scheme.pattern.matcher(s)
                    while (m.find()) {
                        s.setSpan(
                            ForegroundColorSpan(scheme.color),
                            m.start(),
                            m.end(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

            fun removeSpans(e: Editable, type: Class<out CharacterStyle?>) {
                val spans: Array<CharacterStyle> = e.getSpans(0, e.length, type) as Array<CharacterStyle>
                for (span in spans) {
                    e.removeSpan(span)
                }
            }

            inner class ColorScheme(pattern: Pattern, color: Int) {
                val pattern: Pattern
                val color: Int

                init {
                    this.pattern = pattern
                    this.color = color
                }
            }
        })

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