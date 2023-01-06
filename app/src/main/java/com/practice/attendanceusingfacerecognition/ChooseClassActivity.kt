package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button

class ChooseClassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_class)

        // get reference to the string array that we just created
        val semester = resources.getStringArray(R.array.semester)
        val section = resources.getStringArray(R.array.section)
        val course = resources.getStringArray(R.array.course)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        var arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, semester)
        // get reference to the autocomplete text view
        var autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)

        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, section)
        // get reference to the autocomplete text view
        autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)

        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, course)
        // get reference to the autocomplete text view
        autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)

        findViewById<Button>(R.id.takeAttendanceButton).setOnClickListener {
            Intent(this, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}