package com.practice.attendanceusingfacerecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val attended_names = CameraActivity.names
        val reset = findViewById<Button>(R.id.resetBtn)

        for (name in attended_names){
            val stat: TextView
            if(name=="Preetham") {
                stat = findViewById<TextView>(R.id.Preetham)
            }
            else if(name=="Sanjith"){
                stat = findViewById<TextView>(R.id.Sanjith)
            }
            else{
                stat = findViewById<TextView>(R.id.Rahul)
            }

            stat.text = "Yes"

        }


        reset.setOnClickListener {
            attended_names?.clear()
            finish()
        }
    }
}