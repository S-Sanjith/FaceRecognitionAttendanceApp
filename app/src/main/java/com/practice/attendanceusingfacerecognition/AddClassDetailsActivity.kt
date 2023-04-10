package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.practice.attendanceusingfacerecognition.databinding.ActivityAddClassDetailsBinding

class AddClassDetailsActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var listView: ListView
    private lateinit var section: EditText
    private lateinit var sem: EditText
    private var list: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var binding : ActivityAddClassDetailsBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClassDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.editTextTextPersonName)
        button = findViewById(R.id.btnAdd)
        section = findViewById(R.id.Section_input_id)
        sem = findViewById(R.id.Sem_input_id)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        binding.btnAdd.setOnClickListener {
            list.add("${editText.text} (${sem.text}${section.text})")
            editText.setText("")
            section.setText("")
            sem.setText("")
            arrayAdapter.notifyDataSetChanged()
            listView.adapter = arrayAdapter
        }


        val name = intent.getStringExtra("EXTRA_MESSAGE1")
        val mail = intent.getStringExtra("EXTRA_MESSAGE2")
        val dept = intent.getStringExtra("EXTRA_MESSAGE3")
        val password = intent.getStringExtra("EXTRA_MESSAGE4")
        val submit = findViewById<Button>(R.id.submit_id)

        binding.submitId.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                database = FirebaseDatabase.getInstance("https://attendanceusingfacerecog-52ca1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
                val user = User(name, mail, password, dept)

                if (name != null) {
                    database.child(name).setValue(user).addOnSuccessListener {
                        binding.SectionInputId.text.clear()
                        binding.SemInputId.text.clear()

                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }
                }

                it.putExtra("EXTRA_MESSAGE3", name)
                it.putExtra("EXTRA_MESSAGE4", mail)
                it.putExtra("EXTRA_MESSAGE5", dept)
                startActivity(it)
            }
        }

    }
}