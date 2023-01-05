package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.practice.attendanceusingfacerecognition.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.DetailsId.setOnClickListener{
//            val name = binding.nameInputId.text.toString()
//            val mail = binding.mailinputId.text.toString()
//            val password = binding.passwordinputId.text.toString()
//            val dept = binding.departmentinputId.text.toString()
//
//            database = FirebaseDatabase.getInstance("https://attendanceusingfacerecognition-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
//            val user = User(name, mail, password, dept)
//
//            database.child(name).setValue(user).addOnSuccessListener {
//                binding.nameInputId.text.clear()
//                binding.passwordinputId.text.clear()
//
//                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
//            }
            callActivity()
        }
    }

    private fun callActivity() {
        Intent(this,AddClassDetailsActivity::class.java).also {
            it.putExtra("EXTRA_MESSAGE1",binding.nameInputId.text.toString())
            it.putExtra("EXTRA_MESSAGE2",binding.mailinputId.text.toString())
            it.putExtra("EXTRA_MESSAGE3",binding.departmentinputId.text.toString())
            it.putExtra("EXTRA_MESSAGE4",binding.passwordinputId.text.toString())
            startActivity(it)
        }

    }
}