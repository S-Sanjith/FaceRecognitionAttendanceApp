package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val name = intent.getStringExtra("EXTRA_MESSAGE3")
        findViewById<TextView>(R.id.Name_display_id).apply {
            text = name
        }
        val mail = intent.getStringExtra("EXTRA_MESSAGE4")
        findViewById<TextView>(R.id.Mail_display_id).apply {
            text = mail
        }
        val dept= intent.getStringExtra("EXTRA_MESSAGE5")
        findViewById<TextView>(R.id.dept_display_id).apply {
            text = dept
        }

        findViewById<Button>(R.id.chooseClsBtn).setOnClickListener {
            Intent(this,ChooseClassActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}