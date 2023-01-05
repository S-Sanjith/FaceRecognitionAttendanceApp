package com.practice.grocery

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var imageuri : Uri
    private var i: Int = 0
    private var byteArray1: ByteArray? = null
    private var byteArray2: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val py = Python.getInstance()
        val module = py.getModule("disp")
        selectimage()
        findViewById<Button>(R.id.changeBtnId).setOnClickListener {
            try {
                var s = module.callAttr("display", "there").toString()
                findViewById<TextView>(R.id.changeTxtId).text = s
                Toast.makeText(this, s, Toast.LENGTH_LONG).show()

//                selectimage()

//                selectimage()
//                var byteArray2 = contentResolver.openInputStream(imageuri)?.use { it.buffered().readBytes() }
//                var os = ByteArrayOutputStream()
//                var inputStream = this@MainActivity?.contentResolver?.openInputStream(imageuri)
//                var byteArray1 = inputStream?.readBytes()

//                selectimage()

//                while(i == 0) { }

//                inputStream = this@MainActivity?.contentResolver?.openInputStream(imageuri)
//                var byteArray2 = inputStream?.readBytes()

//                s = "hello"
//                s = module.callAttr("getImage", byteArray1, byteArray2).toString()
//                Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

//                val bytes = module.callAttr("plot",
//                    findViewById<EditText>(R.id.etX).text.toString(),
//                    findViewById<EditText>(R.id.etY).text.toString())
//                    .toJava(ByteArray::class.java)
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
//
//                currentFocus?.let {
//                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
//                        .hideSoftInputFromWindow(it.windowToken, 0)
//                }
            } catch (e: PyException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun selectimage() {
        val intent  = Intent()
        intent.type ="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK && data != null && data.data != null)
        {
            imageuri = data?.data!!
            findViewById<ImageView>(R.id.galleryImgId).setImageURI(imageuri)
            i+=1
//            binding.firebaseImage.setImageURI(imageuri)
            if(i == 1) {
                byteArray1 = contentResolver.openInputStream(imageuri)?.use { it.buffered().readBytes() }
            }
            else if(i == 2) {
                byteArray2 = contentResolver.openInputStream(imageuri)?.use { it.buffered().readBytes() }
            }
            else if(i == 3) {
                val py = Python.getInstance()
                val module = py.getModule("disp")
                var s = module.callAttr("getImage", byteArray1, byteArray2).toString()
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
            }
        }
    }

}